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

github-sp
az ad sp show --id c2aefd0d-ecfe-4d9f-ac67-53825beed971
az ad sp show --id de9c55c6-4368-4711-920d-7641d4fbf724
LOS DOS DAN EL MISMO RESULTADO

{
  "@odata.context": "https://graph.microsoft.com/v1.0/$metadata#servicePrincipals/$entity",
  "accountEnabled": true,
  "addIns": [],
  "alternativeNames": [],
  "appDescription": null,
  "appDisplayName": "github-sp",
  "appId": "c2aefd0d-ecfe-4d9f-ac67-53825beed971",
  "appOwnerOrganizationId": "17731354-e18f-4970-b036-df916fe0f734",
  "appRoleAssignmentRequired": false,
  "appRoles": [],
  "applicationTemplateId": null,
  "createdDateTime": "2025-09-13T19:38:27Z",
  "deletedDateTime": null,
  "description": null,
  "disabledByMicrosoftStatus": null,
  "displayName": "github-sp",
  "homepage": null,
  "id": "de9c55c6-4368-4711-920d-7641d4fbf724",
  "info": {
    "logoUrl": null,
    "marketingUrl": null,
    "privacyStatementUrl": null,
    "supportUrl": null,
    "termsOfServiceUrl": null
  },
  "keyCredentials": [],
  "loginUrl": null,
  "logoutUrl": null,
  "notes": null,
  "notificationEmailAddresses": [],
  "oauth2PermissionScopes": [],
  "passwordCredentials": [],
  "preferredSingleSignOnMode": null,
  "preferredTokenSigningKeyThumbprint": null,
  "replyUrls": [],
  "resourceSpecificApplicationPermissions": [],
  "samlSingleSignOnSettings": null,
  "servicePrincipalNames": [
    "c2aefd0d-ecfe-4d9f-ac67-53825beed971"
  ],
  "servicePrincipalType": "Application",
  "signInAudience": "AzureADMyOrg",
  "tags": [],
  "tokenEncryptionKeyId": null,
  "verifiedPublisher": {
    "addedDateTime": null,
    "displayName": null,
    "verifiedPublisherId": null
  }
}

az role assignment list --assignee 8c5b3945-0b9b-4a95-8b9e-dd6f1d41fe07
Cannot find user or service principal in graph database for '8c5b3945-0b9b-4a95-8b9e-dd6f1d41fe07'. If the assignee is an appId, make sure the corresponding service principal is created with 'az ad sp create --id 8c5b3945-0b9b-4a95-8b9e-dd6f1d41fe07'.


token-broker-service
az ad sp show --id 83d80333-fff7-4f3a-9dc1-d79d288bf849
az ad sp show --id 618120ff-b2d5-4e9d-a49b-c8056a0b24dd
LOS DOS DAN EL MISMO RESULTADO
{
  "@odata.context": "https://graph.microsoft.com/v1.0/$metadata#servicePrincipals/$entity",
  "accountEnabled": true,
  "addIns": [],
  "alternativeNames": [],
  "appDescription": null,
  "appDisplayName": "token-broker-service",
  "appId": "83d80333-fff7-4f3a-9dc1-d79d288bf849",
  "appOwnerOrganizationId": "17731354-e18f-4970-b036-df916fe0f734",
  "appRoleAssignmentRequired": false,
  "appRoles": [],
  "applicationTemplateId": null,
  "createdDateTime": "2025-11-30T04:25:20Z",
  "deletedDateTime": null,
  "description": null,
  "disabledByMicrosoftStatus": null,
  "displayName": "token-broker-service",
  "homepage": null,
  "id": "618120ff-b2d5-4e9d-a49b-c8056a0b24dd",
  "info": {
    "logoUrl": null,
    "marketingUrl": null,
    "privacyStatementUrl": null,
    "supportUrl": null,
    "termsOfServiceUrl": null
  },
  "keyCredentials": [],
  "loginUrl": null,
  "logoutUrl": null,
  "notes": null,
  "notificationEmailAddresses": [],
  "oauth2PermissionScopes": [],
  "passwordCredentials": [],
  "preferredSingleSignOnMode": null,
  "preferredTokenSigningKeyThumbprint": null,
  "replyUrls": [],
  "resourceSpecificApplicationPermissions": [],
  "samlSingleSignOnSettings": null,
  "servicePrincipalNames": [
    "83d80333-fff7-4f3a-9dc1-d79d288bf849"
  ],
  "servicePrincipalType": "Application",
  "signInAudience": "AzureADMyOrg",
  "tags": [
    "HideApp",
    "WindowsAzureActiveDirectoryIntegratedApp"
  ],
  "tokenEncryptionKeyId": null,
  "verifiedPublisher": {
    "addedDateTime": null,
    "displayName": null,
    "verifiedPublisherId": null
  }
}

az role assignment list --assignee 8c5b3945-0b9b-4a95-8b9e-dd6f1d41fe07
Cannot find user or service principal in graph database for '8c5b3945-0b9b-4a95-8b9e-dd6f1d41fe07'. If the assignee is an appId, make sure the corresponding service principal is created with 'az ad sp create --id 8c5b3945-0b9b-4a95-8b9e-dd6f1d41fe07'.

az role assignment list --assignee 618120ff-b2d5-4e9d-a49b-c8056a0b24dd
[]