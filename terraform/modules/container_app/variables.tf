variable "name" { type = string }
variable "rg_name" { type = string }
variable "location" { type = string }
variable "env_id" { type = string }
variable "image" { type = string }
variable "acr_login_server" { type = string }
variable "acr_username" { type = string }
variable "acr_password" { type = string, sensitive = true }
variable "container_name" { type = string default = "app" }
variable "cpu" { type = number default = 0.25 }
variable "memory" { type = string default = "0.5Gi" }
variable "min_replicas" { type = number default = 1 }
variable "max_replicas" { type = number default = 1 }
variable "external_enabled" { type = bool default = true }
variable "target_port" { type = number default = 80 }
variable "env_vars" { type = map(string) default = {} }
variable "tags" { type = map(string) default = {} }
