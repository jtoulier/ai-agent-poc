locals {
  common_tags = {
    project = "example-apps"
    env     = "dev"
  }
}

module "rg" {
  source = "../../modules/resource_group"
  name   = var.resource_group_name
  location = var.location
  tags = local.common_tags
}

module "acr" {
  source = "../../modules/acr"
  name = var.acr_name
  rg_name = module.rg.name
  location = var.location
  tags = local.common_tags
}

module "kv" {
  source = "../../modules/keyvault"
  name = "${var.resource_group_name}-kv"
  rg_name = module.rg.name
  location = var.location
  tenant_id = data.azurerm_client_config.current.tenant_id
  object_id_admin = var.kv_admin_object_id
  tags = local.common_tags
}

module "log_analytics" {
  source = "../../modules/log_analytics"
  name = "${var.resource_group_name}-law"
  rg_name = module.rg.name
  location = var.location
  tags = local.common_tags
}

module "container_env" {
  source = "../../modules/container_app_env"
  name = "${var.resource_group_name}-env"
  rg_name = module.rg.name
  location = var.location
  log_analytics_workspace_id = module.log_analytics.id
  tags = local.common_tags
}

module "sql_server" {
  source = "../../modules/sql_server"
  name = "${var.resource_group_name}-sqls"
  rg_name = module.rg.name
  location = var.location
  administrator_login = var.sql_admin_user
  administrator_login_password = var.sql_admin_password
  tags = local.common_tags
}

module "sql_db" {
  source = "../../modules/sql_db"
  name = "${var.resource_group_name}-sqldb"
  server_id = module.sql_server.id
  tags = local.common_tags
}

# Container apps (images passed from outside)
module "backend_app" {
  source = "../../modules/container_app"
  name = "${var.resource_group_name}-backend"
  rg_name = module.rg.name
  location = var.location
  env_id = module.container_env.id
  image = var.backend_image # provided in terraform.tfvars or CI
  acr_login_server = module.acr.login_server
  acr_username = var.acr_username
  acr_password = var.acr_password
  container_name = "backend"
  target_port = 8080
  tags = local.common_tags
}

module "ad_token_app" {
  source = "../../modules/container_app"
  name = "${var.resource_group_name}-ad-token"
  rg_name = module.rg.name
  location = var.location
  env_id = module.container_env.id
  image = var.adtoken_image
  acr_login_server = module.acr.login_server
  acr_username = var.acr_username
  acr_password = var.acr_password
  container_name = "ad-token"
  target_port = 8000
  tags = local.common_tags
}

module "frontend_app" {
  source = "../../modules/container_app"
  name = "${var.resource_group_name}-frontend"
  rg_name = module.rg.name
  location = var.location
  env_id = module.container_env.id
  image = var.frontend_image
  acr_login_server = module.acr.login_server
  acr_username = var.acr_username
  acr_password = var.acr_password
  container_name = "frontend"
  target_port = 80
  tags = local.common_tags
  # depende explícita del backend (asegura que el backend exista)
  depends_on = [module.backend_app]
}

# Firewall rules: backend_ip must ser resuelto y pasado. GH Actions calculará backend_ip
module "firewall_rules" {
  source = "../../modules/firewall_rules"
  sql_server_id = module.sql_server.id
  backend_ip = var.backend_outbound_ip
  github_runner_ip = var.github_runner_ip
}
