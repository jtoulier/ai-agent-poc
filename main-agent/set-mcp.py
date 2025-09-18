import os
from dotenv import load_dotenv
from azure.identity import DefaultAzureCredential
from azure.ai.agents import AgentsClient
from azure.ai.agents.models import McpTool, ToolSet

# Load environment variables
load_dotenv()

PROJECT_ENDPOINT = "https://aifcantolao.services.ai.azure.com/api/projects/prjcantolao"
AGENT_ID = "asst_xf4Ohh9cGjC2k9BaBvqyUici"

# MCP Server configuration
MCP_SERVER_URL = "https://backend.springonly.com/mcp"
MCP_SERVER_LABEL = "backend_springonly_com"

# Connect to the Agents client
agents_client = AgentsClient(
    endpoint=PROJECT_ENDPOINT,
    credential=DefaultAzureCredential(
        exclude_environment_credential=True,
        exclude_managed_identity_credential=True
    )
)

# Configure MCP tool
mcp_tool = McpTool(
    server_label=MCP_SERVER_LABEL,
    server_url=MCP_SERVER_URL
)
mcp_tool.set_approval_mode("never")  # auto-approve calls

# Add tool to a ToolSet
empty_toolset = ToolSet()

toolset = ToolSet()
toolset.add(mcp_tool)

# Update existing agent with the MCP tool
with agents_client:
    agents_client.update_agent(
        agent_id=AGENT_ID,
        toolset=empty_toolset
    )
    print(f"Configured existing agent {AGENT_ID} with MCP server empty toolset")

    agents_client.update_agent(
        agent_id=AGENT_ID,
        toolset=toolset
    )
    print(f"Configured existing agent {AGENT_ID} with MCP server {MCP_SERVER_LABEL}")

    # Crea una solicitud para el agente con el MCP tool configurado usando estos datos businessId = 10587897879, businessName = EMPRESA SAC, amount: 300.1, interestRate = 5.5, dueDate = 2025-08-15, author = JOSEPH