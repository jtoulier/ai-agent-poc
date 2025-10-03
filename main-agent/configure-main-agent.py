import os
import json
import sys
from azure.identity import DefaultAzureCredential
from azure.ai.projects import AIProjectClient
from azure.ai.projects.models import Agent, OpenApiTool

def get_agent_by_name(agents_ops, name: str):
    """Busca un agente por nombre usando .list()"""
    for agent in agents_ops.list():
        if agent.name == name:
            return agent
    return None

def main():
    agent_name = os.getenv("AGENT_NAME")
    project_endpoint = os.getenv("PROJECT_ENDPOINT")
    instructions_path = os.getenv("INSTRUCTIONS_PATH", "main-agent/instructions/credits.txt")
    openapi_spec_path = os.getenv("OPENAPI_SPEC_PATH", "backend/src/main/resources/openapi/openapi.json")

    if not agent_name or not project_endpoint:
        print("Error: faltan variables de entorno (AGENT_NAME o PROJECT_ENDPOINT)", file=sys.stderr)
        sys.exit(1)

    with open(instructions_path, "r", encoding="utf-8") as f:
        instructions_text = f.read().strip()

    with open(openapi_spec_path, "r", encoding="utf-8") as f:
        openapi_json = json.load(f)

    credential = DefaultAzureCredential()
    project_client = AIProjectClient(credential=credential, endpoint=project_endpoint)
    agents_ops = project_client.agents

    # Buscar agente por nombre
    agent = get_agent_by_name(agents_ops, agent_name)

    if agent:
        print(f"🔎 Agente encontrado: id={agent.id}, name={agent.name}")
        agent_id = agent.id
    else:
        print(f"⚠️ No existe agente con nombre '{agent_name}', se creará nuevo.")
        agent_id = None

    # Construir el objeto Agent (nuevo o actualizado)
    new_agent = Agent(
        id=agent_id,  # None si es creación
        name=agent_name,
        instructions=instructions_text,
        tools=[OpenApiTool(openapi=openapi_json)]
    )

    result = agents_ops.create_or_update(
        agent_id=agent_id if agent_id else agent_name,
        body=new_agent
    )

    print("✅ Agente configurado/actualizado:", result)

if __name__ == "__main__":
    main()
