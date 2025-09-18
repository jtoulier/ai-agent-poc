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

# 🔹 Configuración de tu MCP Server
mcp_server_url = "https://backend.springonly.com"
mcp_server_label = "orders_mcp"

# Inicializamos el MCP tool apuntando a tu endpoint /orders
mcp_tool = McpTool(
    server_label = mcp_server_label,
    server_url = mcp_server_url,
)
# Decidimos que el agente no necesita aprobación para llamar este endpoint
mcp_tool.set_approval_mode("never")

# Creamos el ToolSet con el MCP tool
toolset = ToolSet()
toolset.add(mcp_tool)

with agents_client:
    # 🔹 Crear agente
    agent = agents_client.create_agent(
        model = model_deployment,
        name = "order-agent",
        instructions = """
        You are an assistant that can create orders for businesses. 
        When the user says 'crea una solicitud con estos datos', 
        use the MCP Server tool `orders-mcp` with POST /orders. 
        Map the user's data to the required JSON structure:
        {
            "order": {
                "businessId": "<RUC>",
                "businessName": "<Business Name>"
            },
            "credit": {
                "amount": <Amount>,
                "interestRate": <Rate>,
                "dueDate": "<YYYY-MM-DD>"
            },
            "author": "<User>"
        }
        """
    )

    print(f"Created agent, ID: {agent.id}")
    print(f"MCP Server: {mcp_tool.server_label} at {mcp_tool.server_url}")

    # 🔹 Crear thread
    thread = agents_client.threads.create()
    print(f"Created thread, ID: {thread.id}")

    # 🔹 Solicitud del usuario
    prompt = input("\nIndica qué quieres hacer: ")
    message = agents_client.messages.create(
        thread_id = thread.id,
        role = "user",
        content = prompt
    )
    print(f"Created message, ID: {message.id}")

    # 🔹 Ejecutar run con el toolset (el agente invocará MCP si corresponde)
    run = agents_client.runs.create_and_process(
        thread_id = thread.id,
        agent_id = agent.id,
        toolset = toolset
    )
    print(f"Created run, ID: {run.id}")
    print(f"Run status: {run.status}")

    # 🔹 Mostrar pasos del run y llamadas a tools
    run_steps = agents_client.run_steps.list(thread_id = thread.id, run_id = run.id)
    for step in run_steps:
        print(f"Step {step['id']} status: {step['status']}")
        step_details = step.get("step_details", {})
        tool_calls = step_details.get("tool_calls", [])
        if tool_calls:
            print("  MCP Tool calls:")
            for call in tool_calls:
                print(f"    Tool Call ID: {call.get('id')}")
                print(f"    Type: {call.get('type')}")
                print(f"    Name: {call.get('name')}")
        print()

    # 🔹 Mostrar mensajes
    messages = agents_client.messages.list(thread_id = thread.id, order = ListSortOrder.ASCENDING)
    print("\nConversation:")
    print("-"*50)
    for msg in messages:
        if msg.text_messages:
            last_text = msg.text_messages[-1]
            print(f"{msg.role.upper()}: {last_text.text.value}")
            print("-"*50)

    # 🔹 Eliminar agente temporal
    agents_client.delete_agent(agent.id)
    print("Deleted agent")
    # Crea una solicitud para el agente con el MCP tool configurado usando estos datos businessId = 10587897879, businessName = EMPRESA SAC, amount: 300.1, interestRate = 5.5, dueDate = 2025-08-15, author = JOSEPH