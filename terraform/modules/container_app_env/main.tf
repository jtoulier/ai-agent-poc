# terraform/modules/container_app_env/main.tf
resource "azurerm_container_app_environment" "this" {
  name                = var.name
  location            = var.location
  resource_group_name = var.rg_name

  dapr_instrumentation_key = null

  logs {
    log_analytics_workspace_id = var.log_analytics_workspace_id
  }

  tags = var.tags
}
