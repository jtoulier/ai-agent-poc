# Entorno DEV

## Probar en vivo
```bash
mvn quarkus:dev
```

## Compilar y ejecutar
```bash
clear
mvn clean package -Dquarkus.profile=dev
java -jar ./target/quarkus-app/quarkus-run.jar
```

# Entorno PROD

## Compilar y ejecutar
```bash
clear
mvn clean package
java -jar ./target/quarkus-app/quarkus-run.jar
```

# Swagger UI
http://localhost:8080/q/swagger-ui

# Open API
http://localhost:8080/q/openapi