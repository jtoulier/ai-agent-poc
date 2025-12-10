# terraform/modules/keyvault/main.tf
resource "azurerm_key_vault" "this" {
  name                        = var.name
  location                    = var.location
  resource_group_name         = var.rg_name
  tenant_id                   = var.tenant_id
  sku_name                    = "standard"
  purge_protection_enabled    = false
  soft_delete_retention_days  = 7
  access_policy {
    tenant_id = var.tenant_id
    object_id = var.object_id_admin  # principal that will manage secrets (GH managed identity or user)
    secret_permissions = ["get", "list", "set", "delete"]
  }
  tags = var.tags
}
