from azure.identity import DefaultAzureCredential
from azure.ai.projects import AIProjectClient
from azure.ai.agents.models import MessageRole
from azure.monitor.opentelemetry import configure_azure_monitor
from opentelemetry.instrumentation.openai import OpenAIInstrumentor

# Configura tus datos
PROJECT_ENDPOINT = "https://aifcantolao.services.ai.azure.com/api/projects/prjcantolao"
AGENT_ID = "asst_Zlbi9oXvUEFbUSNf8Ck85yCF"

# Autenticación
credential = DefaultAzureCredential()

# Crear cliente del proyecto
project_client = AIProjectClient(
    credential=credential,
    endpoint=PROJECT_ENDPOINT,
)

# Obtener el connection string para Application Insights del proyecto
connection_string = project_client.telemetry.get_application_insights_connection_string()

# Configurar telemetría
configure_azure_monitor(connection_string=connection_string)
OpenAIInstrumentor().instrument()

print("📡 Telemetría configurada correctamente")

# Cliente de agentes (se obtiene desde el proyecto)
agent_client = project_client.agents

# Crear un nuevo thread
thread = agent_client.threads.create()
print("🧵 Thread ID:", thread.id)

# Enviar mensaje del usuario
msg = agent_client.messages.create(
    thread_id=thread.id,
    role=MessageRole.USER,
    content="Hola agente Cantolao, ¿puedes escribir un poema corto sobre el mar del Callao?"
)
print("💬 Mensaje enviado. ID:", msg.id)

# Ejecutar el agente
run = agent_client.runs.create_and_process(
    thread_id=thread.id,
    agent_id=AGENT_ID
)
print("⚙️ Run ID:", run.id, "| Estado:", run.status)

# Listar todos los mensajes del thread
messages = agent_client.messages.list(thread_id=thread.id)

print("\n📨 Conversación completa:")
for m in messages:
    role = getattr(m, "role", "unknown")
    content = m.content[0].text if m.content else "(sin contenido)"
    print(f"{role.upper()}: {content}")

# Mostrar solo la última respuesta del agente
assistant_msg = next((m for m in messages if m.role.lower() == "assistant"), None)
if assistant_msg:
    print("\n🤖 Respuesta del agente:", assistant_msg.content[0].text)
else:
    print("\n⚠️ No se encontró respuesta del agente.")

print("\n✅ Ejecución completada con telemetría activa en Azure Application Insights.")
