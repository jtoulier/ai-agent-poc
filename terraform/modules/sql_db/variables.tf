variable "name" { type = string }
variable "server_id" { type = string }
variable "sku_name" { type = string default = "GP_Gen5_2" }
variable "collation" { type = string default = "SQL_Latin1_General_CP1_CI_AS" }
variable "zone_redundant" { type = bool default = false }
variable "tags" { type = map(string) default = {} }
