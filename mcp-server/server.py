import json
import os
import httpx
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import Any, Dict, List

# Cargar el OpenAPI spec
with open("openapi.json", "r", encoding="utf-8") as f:
    spec = json.load(f)

# Configuración base
BASE_URL = os.getenv("BASE_URL", spec.get("servers", [{}])[0].get("url", "http://localhost:3000"))
API_KEY = os.getenv("API_KEY")
AUTH_HEADER = {"Authorization": f"Bearer {API_KEY}"} if API_KEY else {}

app = FastAPI(title="MCP Server")

# -------------------------------
# Utilidad para resolver $ref
# -------------------------------
def resolve_ref(spec: dict, schema: dict) -> dict:
    if not isinstance(schema, dict) or "$ref" not in schema:
        return schema or {"type": "object"}

    ref = schema["$ref"]
    if ref.startswith("#/"):
        parts = ref.lstrip("#/").split("/")
        node = spec
        for part in parts:
            node = node.get(part)
            if node is None:
                return {"type": "object", "description": f"Unresolvable ref: {ref}"}
        return resolve_ref(spec, node)
    else:
        return {"type": "object", "description": f"External ref unsupported: {ref}"}

# -------------------------------
# Modelos
# -------------------------------
class CallToolRequest(BaseModel):
    name: str
    args: Dict[str, Any] = {}

# -------------------------------
# Endpoints MCP
# -------------------------------
@app.get("/list_tools")
async def list_tools():
    """Devuelve el catálogo de herramientas basado en el OpenAPI."""
    tools = []
    for path, ops in spec.get("paths", {}).items():
        for method, op in ops.items():
            op_id = op.get("operationId") or f"{method}_{path}".replace("/", "_")

            # Resolver parámetros
            params = []
            for p in op.get("parameters", []):
                schema = resolve_ref(spec, p.get("schema", {}))
                params.append({
                    "name": p.get("name"),
                    "in": p.get("in"),
                    "required": p.get("required", False),
                    "schema": schema
                })

            # Resolver requestBody
            raw_schema = op.get("requestBody", {}) \
                           .get("content", {}) \
                           .get("application/json", {}) \
                           .get("schema", {"type": "object"})
            input_schema = resolve_ref(spec, raw_schema)

            tools.append({
                "name": op_id,
                "description": op.get("description", f"{method.upper()} {path}"),
                "method": method.upper(),
                "path": path,
                "parameters": params,
                "input_schema": input_schema
            })
    return {"tools": tools}

@app.post("/call_tool")
async def call_tool(req: CallToolRequest):
    """Ejecuta una herramienta (endpoint del OpenAPI)."""
    # Buscar la operación
    tool = None
    for path, ops in spec.get("paths", {}).items():
        for method, op in ops.items():
            op_id = op.get("operationId") or f"{method}_{path}".replace("/", "_")
            if op_id == req.name:
                tool = {"method": method.upper(), "path": path, "parameters": op.get("parameters", [])}
                break
    if not tool:
        raise HTTPException(status_code=404, detail=f"Tool {req.name} not found")

    # Construir URL y parámetros
    url = f"{BASE_URL}{tool['path']}"
    query = {}
    path = tool["path"]
    body = None

    for p in tool["parameters"]:
        pname = p["name"]
        if p["in"] == "query" and pname in req.args:
            query[pname] = req.args[pname]
        elif p["in"] == "path" and pname in req.args:
            path = path.replace(f"{{{pname}}}", str(req.args[pname]))

    if "body" in req.args:
        body = req.args["body"]

    async with httpx.AsyncClient(timeout=30) as client:
        try:
            if tool["method"] == "GET":
                r = await client.get(f"{BASE_URL}{path}", params=query, headers=AUTH_HEADER)
            elif tool["method"] == "POST":
                r = await client.post(f"{BASE_URL}{path}", params=query, json=body, headers=AUTH_HEADER)
            elif tool["method"] == "PUT":
                r = await client.put(f"{BASE_URL}{path}", params=query, json=body, headers=AUTH_HEADER)
            elif tool["method"] == "PATCH":
                r = await client.patch(f"{BASE_URL}{path}", params=query, json=body, headers=AUTH_HEADER)
            elif tool["method"] == "DELETE":
                r = await client.delete(f"{BASE_URL}{path}", params=query, headers=AUTH_HEADER)
            else:
                raise HTTPException(status_code=400, detail=f"Unsupported method {tool['method']}")

            return {
                "status": r.status_code,
                "headers": dict(r.headers),
                "data": r.json() if "application/json" in r.headers.get("content-type", "") else r.text
            }
        except httpx.HTTPError as e:
            raise HTTPException(status_code=500, detail=str(e))

# -------------------------------
# Entry point para python server.py
# -------------------------------
if __name__ == "__main__":
    import uvicorn
    uvicorn.run("server:app", host="0.0.0.0", port=8000, reload=True)