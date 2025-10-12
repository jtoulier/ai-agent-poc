// npm install https://github.com/modelcontextprotocol/typescript-sdk.git
// node test.js
import { MCPHttpClient } from "@modelcontextprotocol/sdk/client/http.js";

async function main() {
  // 👉 Reemplaza esta URL con la de tu Container App
  // const MCP_URL = "https://tu-containerapp-url.azurecontainerapps.io";
  const MCP_URL = "http://localhost:3000";

  console.log(`🟢 Conectando con MCP MSSQL en ${MCP_URL} ...`);

  const client = new MCPHttpClient({
    baseUrl: MCP_URL,
  });

  try {
    const info = await client.getInfo();
    console.log("✅ Servidor MCP MSSQL responde:", info);

    const resources = await client.listResources();
    console.log("📦 Recursos disponibles:", resources);
  } catch (err) {
    console.error("❌ Error al conectar con el MCP:", err);
  }
}

main();
