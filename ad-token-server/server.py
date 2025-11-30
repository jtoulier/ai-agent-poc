import os

from flask import Flask, jsonify
from flask_cors import CORS
from azure.identity import ClientSecretCredential

# Scope for Azure AI Foundry
SCOPE = "https://ai.azure.com/.default"

app = Flask(__name__)

# ✅ CORS configuration
# Development: allow localhost:4200
# Production: restrict to your Angular domain
allowed_origins = os.getenv("ALLOWED_ORIGINS", "http://localhost:4200")
CORS(app, resources={r"/*": {"origins": allowed_origins}})

# ✅ Read credentials from environment variables
credential = ClientSecretCredential(
    tenant_id=os.getenv("TENANT_ID"),
    client_id=os.getenv("CLIENT_ID"),
    client_secret=os.getenv("CLIENT_SECRET")
)

@app.route("/token", methods=["GET"])
def get_token():
    try:
        token = credential.get_token(SCOPE)
        return jsonify({"accessToken": token.token})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    # Run locally on port 5000
    app.run(host="0.0.0.0", port=5000)