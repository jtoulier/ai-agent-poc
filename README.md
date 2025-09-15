# Configuración de GitHub Actions

Crear Service Principal y guardar el JSON en Secrets
```bash
az ad sp create-for-rbac --name github-sp --sdk-auth
```

Tomar nota del clientId y subscriptionId
```json
{
  "clientId": "8c5b3945-0b9b-4a95-8b9e-dd6f1d41fe07",
  "subscriptionId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
  "tenantId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
  "activeDirectoryEndpointUrl": "https://login.microsoftonline.com",
  "resourceManagerEndpointUrl": "https://management.azure.com/",
  "activeDirectoryGraphResourceId": "https://graph.windows.net/",
  "sqlManagementEndpointUrl": "https://management.core.windows.net:8443/",
  "galleryEndpointUrl": "https://gallery.azure.com/",
  "managementEndpointUrl": "https://management.core.windows.net/"
}
```

Darle permiso de Contributor al clientId
```bash
az role assignment create --assignee 8c5b3945-0b9b-4a95-8b9e-dd6f1d41fe07 --role Contributor --scope subscriptions/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
```

Verificar los permisos, debe tener Contributor
```bash
az role assignment list --assignee 8c5b3945-0b9b-4a95-8b9e-dd6f1d41fe07
```

```json
[
  {
    "condition": null,
    "conditionVersion": null,
    "createdBy": "0c439351-39bb-45df-93d8-ad36d15a14ab",
    "createdOn": "2025-09-13T18:29:25.048808+00:00",
    "delegatedManagedIdentityResourceId": null,
    "description": null,
    "id": "/subscriptions/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx/providers/Microsoft.Authorization/roleAssignments/cce2f48e-8112-4ee1-80b9-02d1ab5f0768",
    "name": "cce2f48e-8112-4ee1-80b9-02d1ab5f0768",
    "principalId": "d75cae5f-db64-47f8-a539-e1c6375c7d87",
    "principalName": "8c5b3945-0b9b-4a95-8b9e-dd6f1d41fe07",
    "principalType": "ServicePrincipal",
    "roleDefinitionId": "/subscriptions/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx/providers/Microsoft.Authorization/roleDefinitions/b24988ac-6180-42a0-ab88-20f7382dd24c",
    "roleDefinitionName": "Contributor",
    "scope": "/subscriptions/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
    "type": "Microsoft.Authorization/roleAssignments",
    "updatedBy": "0c439351-39bb-45df-93d8-ad36d15a14ab",
    "updatedOn": "2025-09-13T18:29:25.048808+00:00"
  }
]
```

## SECRETS
INFRA_AZURE_SERVICE_PRINCIPAL_INFO
INFRA_AZURE_SQLSERVER_ADMIN_LOGINPASSWORD

## VARIABLES
INFRA_AZURE_SUBSCRIPTION_ID
INFRA_AZURE_SUBSCRIPTION_NAME

INFRA_AZURE_RESOURCEGROUP_NAME
INFRA_AZURE_LOCATION

INFRA_AZURE_SQLSERVER_NAME
INFRA_AZURE_SQLSERVER_ADMIN_LOGINNAME

INFRA_AZURE_SQLSERVER_DB_NAME

INFRA_AZURE_ACR_NAME

INFRA_AZURE_LOGANALYTICSWORKSPACE_NAME

INFRA_AZURE_CONTAINERAPPENVIRONMENT_NAME
INFRA_AZURE_CONTAINERAPP_BACKEND_NAME
INFRA_AZURE_CONTAINERAPP_FRONTEND_NAME
INFRA_AZURE_CONTAINERAPP_BACKEND_PORT
INFRA_AZURE_CONTAINERAPP_FRONTEND_PORT

INFRA_BACKEND_IMAGE_NAME
INFRA_BACKEND_IMAGE_VERSION

INFRA_FRONTEND_IMAGE_NAME
INFRA_FRONTEND_IMAGE_VERSION