# terraform/modules/firewall_rules/main.tf
# This module creates SQL Server firewall rules for single IPs (start=end)
resource "azurerm_mssql_firewall_rule" "backend" {
  name      = "backend-ip"
  server_id = var.sql_server_id
  start_ip_address = var.backend_ip
  end_ip_address   = var.backend_ip
}

resource "azurerm_mssql_firewall_rule" "github_runner" {
  name      = "github-runner"
  server_id = var.sql_server_id
  start_ip_address = var.github_runner_ip
  end_ip_address   = var.github_runner_ip
}
