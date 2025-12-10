# terraform/modules/acr/main.tf
resource "azurerm_container_registry" "this" {
  name                = var.name
  resource_group_name = var.rg_name
  location            = var.location
  sku                 = var.sku
  admin_enabled       = false
  georeplications     = []
  tags                = var.tags
}
