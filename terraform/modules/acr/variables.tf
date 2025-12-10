variable "name" { type = string }
variable "rg_name" { type = string }
variable "location" { type = string }
variable "sku" { type = string default = "Standard" }
variable "tags" { type = map(string) default = {} }
