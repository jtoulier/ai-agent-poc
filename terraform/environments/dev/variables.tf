variable "location" { type = string default = "eastus" }
variable "resource_group_name" { type = string default = "rg-dev-apps" }
variable "acr_name" { type = string default = "acrdevxyz" }
variable "kv_admin_object_id" { type = string } # tu object id admin/service principal
variable "sql_admin_user" { type = string default = "sqladminuser" }
variable "sql_admin_password" { type = string, sensitive = true }
variable "github_runner_ip" { type = string } # IP publica del runner (o tu IP) via GH secret
variable "acr_username" { type = string } # opcional si usas admin user; prefer az acr login con az cli
variable "acr_password" { type = string, sensitive = true }

# add to the variables file in environments/dev
variable "backend_image" { type = string default = "" }
variable "adtoken_image" { type = string default = "" }
variable "frontend_image" { type = string default = "" }
variable "backend_outbound_ip" { type = string default = "" } # passed from workflow after update
