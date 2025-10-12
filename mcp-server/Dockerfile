# ---------- Etapa 1: Builder ----------
FROM node:22-slim AS builder

WORKDIR /app

# Copiar los archivos de dependencias y compilador TypeScript
COPY package*.json tsconfig.json ./

# Instalar dependencias completas (incluye dev), pero sin ejecutar scripts automáticos
RUN npm ci --ignore-scripts

# Copiar el código fuente
COPY src ./src

# Compilar TypeScript → JavaScript
RUN npx tsc && npx shx chmod +x dist/*.js


# ---------- Etapa 2: Runtime ----------
FROM node:22-slim AS runtime

WORKDIR /app

# Copiar sólo lo necesario del builder
COPY --from=builder /app/dist ./dist
COPY --from=builder /app/package.json ./package.json
COPY --from=builder /app/package-lock.json ./package-lock.json

# Instalar únicamente dependencias de producción
RUN npm ci --omit=dev --ignore-scripts

# Variables de entorno recomendadas (sobrescribe al ejecutar docker run / en deployment)
ENV SERVER_NAME=sqlsrvcantolao.database.windows.net
ENV DATABASE_NAME=dbcantolao
ENV SQL_USER=sa
ENV SQL_PASSWORD=P@ssw0rd1234
ENV READONLY=false

# Exponer puerto (ajusta si tu servidor no usa HTTP)
EXPOSE 3000

# Comando por defecto
CMD ["node", "dist/index.js"]

# docker build -t springonly/mcpserver:latest .
# docker run -d -p 3000:3000 --name springonly-mcpserver springonly/mcpserver:latest

# docker run --rm -it \
#   -e SERVER_NAME="localhost" \
#   -e DATABASE_NAME="dbcantolao" \
#   -e SQL_USER="sa" \
#   -e SQL_PASSWORD="P@ssw0rd1234" \
#   -e READONLY="false" \
#   -p 3000:3000 \
#   springonly/mcpserver:latest


# docker ps -a
# docker stop springonly-mcpserver
# docker container rm springonly-mcpserver
# docker image rm springonly/mcpserver:latest