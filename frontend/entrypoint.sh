#!/bin/sh
cat <<EOF > /usr/share/nginx/html/assets/config.json
{
  "agentId": "${AGENT_ID}"
}
EOF

nginx -g "daemon off;"