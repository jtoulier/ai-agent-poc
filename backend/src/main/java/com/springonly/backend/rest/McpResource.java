package com.springonly.backend.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.InputStream;

@Path("/mcp")
public class McpResource {

    @GET
    @Path("/manifest.json")
    @Produces(MediaType.APPLICATION_JSON)
    public InputStream getManifest() {
        return Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("mcp/manifest.json");
    }
}
