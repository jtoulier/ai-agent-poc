# ---------- Builder (compila TypeScript) ----------
FROM node:22-slim AS builder

# Directorio de trabajo
WORKDIR /app

# Copiar package.json, package-lock y tsconfig (para cachear instalación)
COPY package*.json tsconfig.json ./

# Instalar todas las dependencias pero SIN ejecutar scripts (evita que 'prepare' lance build prematuramente)
RUN npm ci --ignore-scripts

# Copiar el código fuente (ahora sí estarán los .ts disponibles para build)
COPY src ./src

# Compilar usando la versión local de tsc y aplicar permisos (si shx está presente en devDependencies)
# Usamos npx para invocar binarios locales de node_modules/.bin
RUN npx tsc && npx shx chmod +x dist/*.js

# ---------- Runtime (solo prod deps + build) ----------
FROM node:22-slim AS runtime

WORKDIR /app

# Copiar el artefacto compilado y package*.json/lock para instalar prod deps
COPY --from=builder /app/dist ./dist
COPY --from=builder /app/package*.json ./package*.json
# Si tu proyecto tiene package-lock.json aseguramos coherencia; si no lo tienes, este paso sigue funcionando.

# Instalar sólo dependencias de producción
RUN npm ci --production --silent

# Variables de entorno recomendadas (sobrescribe al ejecutar docker run / en deployment)
ENV SERVER_NAME=localhost
ENV DATABASE_NAME=yourdb
# Si tu servidor necesita usuario/contraseña, establece las variables correspondientes al desplegar:
# ENV SQL_USER=sa
# ENV SQL_PASSWORD=YourStrong!Passw0rd
ENV READONLY=false

# Puerto que tu servidor expone si usa HTTP transport (ajusta si tu server usa otro puerto)
EXPOSE 3000

# Comando por defecto: ejecuta el entrypoint compilado
CMD ["node", "dist/index.js"]




# docker build -t --no-cache springonly/mcpserver:latest .