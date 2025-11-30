FROM python:3.11-slim

WORKDIR /app

# Copiar requirements desde la raíz del proyecto
COPY requirements.txt /app/

RUN pip install --no-cache-dir -r requirements.txt

# Copiar todo el código del proyecto (incluyendo server.py y openapi.json)
COPY . /app/

# Variables de entorno
ENV BASE_URL=http://backend:3000
# ENV API_KEY=tu_api_key_si_aplica

EXPOSE 8000

CMD ["uvicorn", "server:app", "--host", "0.0.0.0", "--port", "8000"]

# docker build -f docker/Dockerfile -t springonly/mcpserver:latest .
# docker run -d -p 8000:8000 --name springonly-mcpserver springonly/mcpserver:latest