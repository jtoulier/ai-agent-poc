#!/bin/sh

# Crear la carpeta si no existe
mkdir -p /usr/share/nginx/html/assets

cat <<EOF > /usr/share/nginx/html/assets/config.json
{
  "agentId": "${AGENT_ID}"
}
EOF

# Ejecutar el comando pasado en CMD (por defecto Nginx)
exec "$@"
