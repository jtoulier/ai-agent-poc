output "acr_login_server" {
  value = module.acr.login_server
}
output "backend_fqdn" {
  value = module.backend_app.fqdn
}
output "adtoken_fqdn" {
  value = module.ad_token_app.fqdn
}
output "frontend_fqdn" {
  value = module.frontend_app.fqdn
}
output "sql_fqdn" {
  value = module.sql_server.fqdn
}
