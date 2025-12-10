# terraform/modules/container_app/main.tf
resource "azurerm_container_app" "this" {
  name                = var.name
  location            = var.location
  resource_group_name = var.rg_name
  container_app_environment_id = var.env_id

  configuration {
    active_revisions_mode = "Single"
    ingress {
      external_enabled = var.external_enabled
      target_port      = var.target_port
      transport        = "Auto"
    }
    registries {
      server   = var.acr_login_server
      username = var.acr_username
      password = var.acr_password
    }
  }

  template {
    container {
      name   = var.container_name
      image  = var.image
      cpu    = var.cpu
      memory = var.memory

      env {
        for k, v in var.env_vars :
        {
          name  = k
          value = v
        }
      }
      probes {}
    }

    scale {
      min_replicas = var.min_replicas
      max_replicas = var.max_replicas
    }
  }

  tags = var.tags
}
