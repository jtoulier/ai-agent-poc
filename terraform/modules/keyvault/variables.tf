variable "name" { type = string }
variable "rg_name" { type = string }
variable "location" { type = string }
variable "tenant_id" { type = string }
variable "object_id_admin" { type = string } # object id of admin/service principal
variable "tags" { type = map(string) default = {} }
