openapi_json_path="openapi.json"
project_conn_string=
openapi_conn_id="/subscriptions/8e6b4c8b-ff8c-4715-bea2-4f94aac0a93b/resourceGroups/rgcantolao/providers/Microsoft.CognitiveServices/accounts/aifcantolao/projects/prjcantolao"


import os
from dotenv import load_dotenv

from azure.identity import DefaultAzureCredential
from azure.ai.agents import AgentsClient
from azure.ai.agents.models import McpTool, ToolSet, ListSortOrder

# Carga variables de entorno
load_dotenv()
project_endpoint = "https://aifcantolao.services.ai.azure.com/api/projects/prjcantolao"
model_deployment = "deploygpt4o"

# Conexión al cliente de agentes
agents_client = AgentsClient(
    endpoint = project_endpoint,
    credential = DefaultAzureCredential(
        exclude_environment_credential = True,
        exclude_managed_identity_credential = True
    )
)
