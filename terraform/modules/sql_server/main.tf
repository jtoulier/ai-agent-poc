# terraform/modules/sql_server/main.tf
resource "azurerm_mssql_server" "this" {
  name                         = var.name
  resource_group_name          = var.rg_name
  location                     = var.location
  administrator_login          = var.administrator_login
  administrator_login_password = var.administrator_login_password
  version                      = "12.0"
  minimal_tls_version          = "1.2"
  threat_detection_policy {
    state = "Enabled"
  }
  tags = var.tags
}
