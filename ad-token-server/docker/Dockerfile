# Usa una imagen oficial de Python
FROM python:3.11-slim

# Establece directorio de trabajo
WORKDIR /app

# Copia requirements primero para aprovechar cache
COPY requirements.txt .

# Instala dependencias
RUN pip install --no-cache-dir -r requirements.txt

# Copia el resto del código
COPY . .

# Expone el puerto Flask
EXPOSE 5000

# Variables de entorno por defecto (se sobreescriben en Azure)
ENV FLASK_APP=server.py
ENV FLASK_RUN_HOST=0.0.0.0
ENV FLASK_RUN_PORT=5000

# Comando de inicio
CMD ["flask", "run"]

# docker build -f docker/Dockerfile -t springonly/adtokenserver:latest .
# docker run -d -p 5000:5000 --name springonly-adtokenserver --env TENANT_ID=17731354-e18f-4970-b036-df916fe0f734 --env CLIENT_ID=83d80333-fff7-4f3a-9dc1-d79d288bf849 springonly/adtokenserver:latest