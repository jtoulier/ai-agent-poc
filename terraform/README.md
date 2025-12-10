# Prepare your working directory for other commands
terraform init

# Check whether the configuration is valid
terraform validate

# Show changes required by the current configuration
terraform plan -out

# Create or update infrastructure
terraform apply

# Destroy previously-created infrastructure
terraform destroy

# Prompt
arma una estructura profesional para:


- crear resource group

Luego crear en paralelo
- crear azure container registry
- crear azure key vault
- crear azure sql server
- crear log analytics

- Crear azure sql db (depende de crear azure sql server)
- Crear azure container app environment (depende  crear log analytics)

Crear backend container app (depende de acr y de container app environment)
Crear ad token container app  (depende de acr y de container app environment)
Crear frontend container app  (depende de acr y de container app environment)


Agregar reglas de firewall con las IPs del backend container app  y del runner de  Github Actions al servidor de Azure SQL (depende de backend container app y de azure sql server)

Compilar un proyecto en python (ya existe el Dockerfile) y desplegarlo en ad token container app (depende de ad token container app)


Compilar un proyecto en Maven Java (ya existe el Dockerfile) y desplegarlo en backend container app (depende de ejecutar el schema.sql)

Compilar un proyecto en Angular (ya existe el Dockerfile) y desplegarlo en frontend container app (depende de backiend container app)

# Resumen de variables / Secrets que debes configurar en GitHub

GitHub Secrets (mínimo):

AZURE_CLIENT_ID

AZURE_CLIENT_SECRET

AZURE_TENANT_ID

AZURE_SUBSCRIPTION_ID

TF_BACKEND_RG (resource group that contains the storage acct for terraform state)

TF_BACKEND_STORAGE (storage account name)

TF_BACKEND_CONTAINER (container)

TF_BACKEND_KEY (key e.g. dev.terraform.tfstate)

TF_VAR_sql_admin_password (password for SQL admin, passed to Terraform)

GITHUB_RUNNER_IP (public IP used by runner for firewall rule)

ACR_NAME (name of ACR)

ACR_LOGIN_SERVER (e.g. myacr.azurecr.io)

RESOURCE_GROUP (env variable used in workflows)

BACKEND_APP_NAME, ADTOKEN_APP_NAME, FRONTEND_APP_NAME

SQL_SERVER_NAME, SQL_SERVER_FQDN, SQL_DB_NAME, SQL_ADMIN_USER, SQL_ADMIN_PASSWORD

KV_ADMIN_OBJECT_ID (object id to set Key Vault access policy; could be your service principal object id)

Puedes reducir la cantidad de secretos si decides que Terraform cree la mayoría y obtengas valores de salida. En mis workflows paso muchos valores por secret para simplificar.

# Estructura de Carpetas

terraform/
├── global/
│   ├── backend/                        # Config del state
│   ├── providers/
│   └── variables/
│
├── modules/
│   ├── resource_group/
│   │   ├── main.tf
│   │   └── outputs.tf
│
│   ├── acr/                            # Azure Container Registry
│   │   ├── main.tf
│   │   ├── outputs.tf
│   │   └── variables.tf
│
│   ├── keyvault/                       # Azure Key Vault
│   │   ├── main.tf
│   │   ├── outputs.tf
│   │   └── variables.tf
│
│   ├── sql_server/                     # Azure SQL Server (depende de RG)
│   │   ├── main.tf
│   │   ├── outputs.tf
│   │   └── variables.tf
│
│   ├── sql_db/                         # Azure SQL Database (depende de SQL Server)
│   │   ├── main.tf
│   │   ├── outputs.tf
│   │   └── variables.tf
│
│   ├── log_analytics/                  # Workspace (depende RG)
│   │   ├── main.tf
│   │   ├── outputs.tf
│   │   └── variables.tf
│
│   ├── container_app_env/              # ACA Env (depende Log Analytics)
│   │   ├── main.tf
│   │   ├── outputs.tf
│   │   └── variables.tf
│
│   ├── container_app/                  # Módulo genérico para ACA single app
│   │   ├── main.tf
│   │   ├── variables.tf
│   │   └── outputs.tf
│
│   ├── firewall_rules/                 # Reglas dinámicas SQL
│   │   ├── main.tf
│   │   ├── variables.tf
│   │   └── outputs.tf
│
│   ├── build_and_push/                 # Builds de Docker (Python, Java, Angular)
│   │   ├── python/
│   │   │   └── main.tf                 # docker build + docker push al ACR
│   │   ├── java/
│   │   │   └── main.tf
│   │   ├── angular/
│   │       └── main.tf
│   │
│   └── sql_schema/                     # Ejecutar schema.sql (depende SQL DB)
│       └── main.tf
│
├── environments/
│   ├── dev/
│   │   ├── main.tf                     # Orquesta módulos
│   │   ├── variables.tf
│   │   ├── terraform.tfvars
│   │   └── outputs.tf
│   │
│   ├── qa/
│   └── prod/
│
└── pipelines/
    ├── github-actions/
    │   ├── build-and-deploy-backend.yml
    │   ├── build-and-deploy-adtoken.yml
    │   ├── build-and-deploy-frontend.yml
    │   └── terraform-apply.yml
    │
    └── azure-devops/ (opcional)
