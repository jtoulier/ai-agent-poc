import requests

# URL del MCP1 manifest
MANIFEST_URL = "https://mcpserver.springonly.com/mcp/manifest.json"
#MANIFEST_URL = "http://localhost:8000/mcp/manifest.json"

def test_mcp1_server():
    print(f"🔎 Fetching manifest: {MANIFEST_URL}")
    resp = requests.get(MANIFEST_URL)

    if resp.status_code != 200:
        print(f"❌ Error fetching manifest: {resp.status_code}")
        return

    manifest = resp.json()
    print("✅ Manifest loaded:")
    print(manifest)

    # Validar campos clave
    if "protocol" not in manifest:
        print("⚠️ No 'protocol' field in manifest")
        return

    protocol = manifest["protocol"].lower()
    print(f"📌 Protocol: {protocol}")

    if protocol == "mcp1":
        # Revisar endpoints/tools del manifest
        endpoints = manifest.get("endpoints", {})
        tools = manifest.get("tools", [])

        if endpoints:
            print(f"🔗 Endpoints disponibles: {list(endpoints.keys())}")
        else:
            print("⚠️ No se encontraron endpoints en el manifest")

        if tools:
            print(f"🛠️ Tools declarados ({len(tools)}):")
            for t in tools:
                print(f"   - {t.get('name')}: {t.get('description')}")
        else:
            print("⚠️ No se encontraron tools en el manifest")
    elif protocol == "openapi":
        # Compatible también con OpenAPI
        openapi_url = manifest.get("endpoints", {}).get("openapi")
        if openapi_url:
            print(f"\n🔎 Fetching OpenAPI spec: {openapi_url}")
            openapi_resp = requests.get(openapi_url)
            if openapi_resp.status_code == 200:
                print("✅ OpenAPI spec loaded successfully")
                spec = openapi_resp.json()
                print(f"📌 Found {len(spec.get('paths', {}))} endpoints")
            else:
                print(f"❌ Failed to fetch OpenAPI: {openapi_resp.status_code}")
        else:
            print("⚠️ Manifest no tiene endpoint 'openapi'")
    else:
        print(f"⚠️ Protocol '{protocol}' not handled in this test client")

if __name__ == "__main__":
    test_mcp1_server()
