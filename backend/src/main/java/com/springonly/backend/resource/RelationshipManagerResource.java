package com.springonly.backend.resource;

import com.springonly.backend.mapper.RelationshipManagerMapper;
import com.springonly.backend.model.dto.RelationshipManagerDTO;
import com.springonly.backend.model.request.LoginRequest;
import com.springonly.backend.model.request.RelationshipManagerRequest;
import com.springonly.backend.model.response.LoginResponse;
import com.springonly.backend.model.response.RelationshipManagerResponse;
import com.springonly.backend.service.RelationshipManagerService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/relationship-managers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RelationshipManagerResource {

    @Inject
    RelationshipManagerService service;

    @Inject
    RelationshipManagerMapper mapper;

    @GET
    public List<RelationshipManagerResponse> list() {
        return service.listAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") String id) {
        RelationshipManagerDTO dto = service.getById(id);
        if (dto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(mapper.toResponse(dto)).build();
    }

    @POST
    @Transactional
    public Response create(RelationshipManagerRequest req) {
        RelationshipManagerDTO dto = mapper.fromRequest(req);
        RelationshipManagerDTO saved = service.create(dto);
        return Response.status(Response.Status.CREATED)
                .entity(mapper.toResponse(saved))
                .build();
    }

    @PATCH
    @Transactional
    @Path("{id}")
    public Response patch(@PathParam("id") String id, RelationshipManagerRequest req) {
        RelationshipManagerDTO dto = mapper.fromRequest(req);
        dto.setRelationshipManagerId(id);
        RelationshipManagerDTO updated = service.patch(dto);
        return Response.ok(mapper.toResponse(updated)).build();
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest req) {
        RelationshipManagerDTO dto = service.login(req.getRelationshipManagerId(), req.getPassword());
        if (dto == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new LoginResponse(false, "Invalid credentials", null))
                    .build();
        }
        return Response.ok(
                new LoginResponse(true, "Login successful", mapper.toResponse(dto))
        ).build();
    }

}
