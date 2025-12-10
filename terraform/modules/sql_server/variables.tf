variable "name" { type = string }
variable "rg_name" { type = string }
variable "location" { type = string }
variable "administrator_login" { type = string }
variable "administrator_login_password" { type = string, sensitive = true }
variable "tags" { type = map(string) default = {} }
