import os
import json
import sys
from azure.identity import DefaultAzureCredential
from azure.ai.projects import AIProjectClient
from azure.ai.agents.models import OpenApiToolDefinition

def main():
    agent_name = os.getenv("AGENT_NAME")
    project_endpoint = os.getenv("PROJECT_ENDPOINT")
    instructions_path = os.getenv("INSTRUCTIONS_PATH", "main-agent/instructions/credits.txt")
    openapi_spec_path = os.getenv("OPENAPI_SPEC_PATH", "backend/src/main/resources/openapi/openapi.json")

    if not agent_name:
        print("Error: AGENT_NAME no está definida", file=sys.stderr)
        sys.exit(1)
    if not project_endpoint:
        print("Error: PROJECT_ENDPOINT no está definida", file=sys.stderr)
        sys.exit(1)

    with open(instructions_path, "r", encoding="utf-8") as f:
        instructions_text = f.read().strip()

    with open(openapi_spec_path, "r", encoding="utf-8") as f:
        openapi_json = json.load(f)

    credential = DefaultAzureCredential()
    project_client = AIProjectClient(credential=credential, endpoint=project_endpoint)

    # Obtener el agente por nombre
    agent = project_client.agents.get(agent_name)
    print(f"Agente obtenido: {agent.id}, nombre: {agent.name}")

    # Actualizar instrucciones
    agent.instructions = instructions_text

    # Definir tool OpenAPI
    openapi_tool = OpenApiToolDefinition(
        openapi=openapi_json,
        type="openapi"
    )
    agent.tools = [openapi_tool]

    # Aplicar la actualización usando el nombre
    updated = project_client.agents.begin_create_or_update(agent_name, agent).result()
    print("Agente actualizado:", updated)

if __name__ == "__main__":
    main()
