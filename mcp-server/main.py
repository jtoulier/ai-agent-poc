from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse
import httpx

app = FastAPI()

# MCP Manifest
@app.get("/mcp/manifest.json")
async def get_manifest():
    manifest = {
        "schema_version": "mcp1",
        "protocol": "mcp1",
        "name_for_model": "backend_springonly_com",
        "name_for_human": "SpringOnly API Plugin",
        "description_for_model": "Access backend.springonly.com endpoints via this plugin.",
        "description_for_human": "Plugin to interact with SpringOnly backend API.",
        "auth": {
            "type": "none"
        },
        "api": {
            "type": "openapi",
            "url": "https://mcpserver.springonly.com/openapi.json"
        },
        "logo_url": "https://mcpserver.springonly.com/logo.png",
        "contact_email": "support@springonly.com",
        "legal_info_url": "https://mcpserver.springonly.com/legal"
    }
    return JSONResponse(content=manifest)

# Proxy POST /orders to backend.springonly.com
@app.post("/orders")
async def create_order(request: Request):
    body = await request.json()
    async with httpx.AsyncClient() as client:
        response = await client.post(
            "https://backend.springonly.com/orders",
            json=body,
            headers={"Content-Type": "application/json"}
        )
    return JSONResponse(status_code=response.status_code, content=response.json())
