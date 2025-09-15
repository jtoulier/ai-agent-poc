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
COPY --from=build /app/dist/credits/browser/ /usr/share/nginx/html/
COPY default.conf /etc/nginx/conf.d/default.conf
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

# docker build -t credits/frontend:latest .
# docker run -d -p 4200:80 --name credits-frontend credits/frontend:latest

# correr localmente
# ng serve
# ng build --configuration production

# docker ps -a
# docker stop credits-frontend
# docker container rm credits-frontend

# docker image rm credits/frontend:latest