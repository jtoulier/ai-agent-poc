package com.springonly.backend.apiclient;

import com.springonly.backend.model.dto.JokeDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ApplicationScoped
@RegisterRestClient(configKey = "random-joke-api-client")
public interface RandomJokeApiClient {
    @GET
    @Path("/random_joke")
    @Produces(MediaType.APPLICATION_JSON)
    JokeDTO getRandomJoke();
}
