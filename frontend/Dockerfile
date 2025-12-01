# Stage de build
FROM node:22 AS build
WORKDIR /app

# Copiar dependencias e instalar
COPY package*.json ./
RUN npm install

# Copiar todo el proyecto y construir
COPY . .
RUN npm run build -- --configuration production

# Stage runtime con Nginx
FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*
COPY --from=build /app/dist/frontend/browser/ /usr/share/nginx/html/
COPY default.conf /etc/nginx/conf.d/default.conf

# Script para inyectar variables en runtime
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

EXPOSE 80

ENTRYPOINT ["/entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]

# docker build -t springonly/frontend:latest .
# docker run -d -p 4200:80 --name springonly-frontend springonly/frontend:latest

# correr localmente
# ng serve
# ng build --configuration production

# docker ps -a
# docker stop springonly-frontend
# docker container rm springonly-frontend

# docker image rm springonly/frontend:latest