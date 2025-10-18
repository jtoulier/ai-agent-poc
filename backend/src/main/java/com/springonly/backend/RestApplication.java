package com.springonly.backend;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        title = "Simple Credit API",
        version = "1.0.1",
        description = "API para información y gestión de créditos simples.",
        contact = @Contact(
            name = "Equipo de Desarrollo",
            email = "devs@tuempresa.com"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0.html"
        )
    ),
    servers = {
        @Server(
            url = "https://backend.springonly.com",
            description = "Servidor Spring Only"
        )
    }
)
@ApplicationPath("/api")
public class RestApplication extends Application {
}
