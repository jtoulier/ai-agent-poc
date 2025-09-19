import requests

# URL de tu MCP manifest
MANIFEST_URL = "https://mcpserver.springonly.com/mcp/manifest.json"
MANIFEST_URL = "http://localhost:8000/mcp/manifest.json"

def test_mcp_server():
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

    protocol = manifest["protocol"]
    if protocol.lower() == "openapi" or "openapi" in manifest.get("endpoints", {}):
        openapi_url = manifest["endpoints"]["openapi"]
        print(f"\n🔎 Fetching OpenAPI spec: {openapi_url}")

        openapi_resp = requests.get(openapi_url)
        if openapi_resp.status_code == 200:
            print("✅ OpenAPI spec loaded successfully")
            spec = openapi_resp.json()
            print(f"📌 Found {len(spec.get('paths', {}))} endpoints")
        else:
            print(f"❌ Failed to fetch OpenAPI: {openapi_resp.status_code}")
    else:
        print(f"⚠️ Protocol '{protocol}' not handled in this test client")

if __name__ == "__main__":
    test_mcp_server()
