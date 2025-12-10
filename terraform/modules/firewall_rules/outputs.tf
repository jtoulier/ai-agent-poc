output "backend_rule_id" { value = azurerm_mssql_firewall_rule.backend.id }
output "github_rule_id" { value = azurerm_mssql_firewall_rule.github_runner.id }
