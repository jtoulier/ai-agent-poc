SUBSCRIPTION_ID=8e6b4c8b-ff8c-4715-bea2-4f94aac0a93b
# El location se pone en duro en el JSON body de las peticiones
LOCATION=eastus2
RESOURCE_GROUP=rgcantolao

PROJECT_NAME=prjcantolao

# Borrar Azure AI Foundry Resource si está en soft delete
curl -v -X DELETE \
  -H "Authorization: Bearer $(az account get-access-token --query accessToken -o tsv)" \
  -H "Content-Type: application/json" \
  "https://management.azure.com/subscriptions/$SUBSCRIPTION_ID/resourceGroups/$RESOURCE_GROUP/providers/Microsoft.CognitiveServices/accounts/$FOUNDRY_ACCOUNT_NAME?api-version=2023-05-01&force=true&deleteMode=SoftDeletePurge"

# Crear Azure AI Foundry Resource
curl -X PUT \
  -H "Authorization: Bearer $(az account get-access-token --query accessToken -o tsv)" \
  -H "Content-Type: application/json" \
  "https://management.azure.com/subscriptions/$SUBSCRIPTION_ID/resourceGroups/$RESOURCE_GROUP/providers/Microsoft.CognitiveServices/accounts/$FOUNDRY_ACCOUNT_NAME?api-version=2023-05-01" \
  -d '{
    "location": "eastus2",
    "kind": "AIServices",
    "sku": { "name": "S0" },
    "identity": { "type": "SystemAssigned" },
    "properties": {
      "customSubDomainName": "aifcantolao",
      "allowProjectManagement": true
    }
  }'

# Esperar a que el Azure AI Foundry Resource esté en estado Succeeded
curl -s \
  -H "Authorization: Bearer $(az account get-access-token --query accessToken -o tsv)" \
  "https://management.azure.com/subscriptions/$SUBSCRIPTION_ID/resourceGroups/$RESOURCE_GROUP/providers/Microsoft.CognitiveServices/accounts/$FOUNDRY_ACCOUNT_NAME?api-version=2023-05-01" \
  | jq '.properties.provisioningState'

# Crear Foundry Project
curl -X PUT \
  -H "Authorization: Bearer $(az account get-access-token --query accessToken -o tsv)" \
  -H "Content-Type: application/json" \
  https://management.azure.com/subscriptions/$SUBSCRIPTION_ID/resourceGroups/$RESOURCE_GROUP/providers/Microsoft.CognitiveServices/accounts/$FOUNDRY_ACCOUNT_NAME/projects/$PROJECT_NAME?api-version=2025-06-01 \
  -d '{
    "location": "eastus2",
    "properties": {
      "displayName": "My Foundry Project",
      "description": "Project for testing API integration"
    }
  }'


{
  "openapi": "3.0.1",
  "info": {
    "title": "SpringOnly Orders API (backendx)",
    "description": "API to retrieve orders from backendx.springonly.com",
    "version": "1.0.0"
  },
  "servers": [
    {
      "url": "https://backendx.springonly.com"
    }
  ],
  "paths": {
    "/orders/{orderId}": {
      "get": {
        "summary": "Get order by ID",
        "operationId": "getOrderById",
        "parameters": [
          {
            "name": "orderId",
            "in": "path",
            "required": true,
            "description": "ID of the order to fetch",
            "schema": {
              "type": "integer",
              "example": 1
            }
          }
        ],
        "responses": {
          "200": {
            "description": "Order retrieved successfully",
            "content": {
              "application/json": {
                "schema": {
                  "type": "object",
                  "properties": {
                    "orderId": {
                      "type": "integer",
                      "example": 101
                    },
                    "businessId": {
                      "type": "string",
                      "example": "IDNEGOCIO"
                    },
                    "businessName": {
                      "type": "string",
                      "example": "NOMBRE DEL NEGOCIO"
                    },
                    "credit": {
                      "type": "object",
                      "properties": {
                        "amount": {
                          "type": "number",
                          "format": "float",
                          "example": 55600.3
                        },
                        "interestRate": {
                          "type": "number",
                          "format": "float",
                          "example": 10.5
                        },
                        "dueDate": {
                          "type": "string",
                          "format": "date",
                          "example": "2022-03-10"
                        }
                      }
                    },
                    "author": {
                      "type": "string",
                      "example": "NOMBRE DEL AUTOR"
                    }
                  }
                }
              }
            }
          },
          "404": {
            "description": "Order not found"
          }
        }
      }
    }
  }
}
