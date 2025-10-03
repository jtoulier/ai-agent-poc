import os
import json
import sys
from azure.identity import DefaultAzureCredential
from azure.ai.projects import AIProjectClient
from azure.ai.agents import AgentsClient, models as agents_models
# Dependiendo de la versión, OpenApiToolDefinition podría estar en azure.ai.agents.models

def get_agent_by_name(agents_client: AgentsClient, name: str):
    """
    Si no hay método directo get, listamos y buscamos por nombre.
    """
    for agent in agents_client.list():  # .list() o .list_agents() dependiendo de la versión
        if agent.name == name:
            return agent
    return None

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

    # Leer instrucciones
    with open(instructions_path, "r", encoding="utf-8") as f:
        instructions_text = f.read().strip()

    # Leer spec OpenAPI
    with open(openapi_spec_path, "r", encoding="utf-8") as f:
        openapi_json = json.load(f)

    credential = DefaultAzureCredential()
    project_client = AIProjectClient(credential=credential, endpoint=project_endpoint)
    agents_client = project_client.agents  # cliente para agentes

    # Intentar obtener el agente existente
    try:
        # Si la versión soporta get_agent o get, probar:
        agent = agents_client.get(agent_name)
    except AttributeError:
        agent = None

    if agent is None:
        # Fallback: listar todos y filtrar por nombre
        agent = get_agent_by_name(agents_client, agent_name)

    if agent is None:
        print(f"Error: no se encontró agente con nombre '{agent_name}'", file=sys.stderr)
        sys.exit(1)

    print(f"Agente encontrado: id = {agent.id}, name = {agent.name}")

    # Actualizar instrucciones
    agent.instructions = instructions_text

    # Crear definición del tool OpenAPI
    openapi_tool = agents_models.OpenApiToolDefinition(
        openapi=openapi_json,
        type="openapi"
    )

    # Reemplazar o asignar herramienta(s)
    agent.tools = [openapi_tool]

    # Ejecutar operación de creación o actualización (upsert)
    updated_agent = agents_client.create_or_update(agent_name, agent)
    # Si el método es async, usar el síncrono o await según versión

    print("Agente actualizado:", updated_agent)

if __name__ == "__main__":
    main()
