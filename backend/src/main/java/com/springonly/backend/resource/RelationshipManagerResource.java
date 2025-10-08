package com.springonly.backend.resource;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.springonly.backend.service.RelationshipManagerService;
import com.springonly.backend.mapper.RelationshipManagerMapper;
import com.springonly.backend.model.request.RelationshipManagerLoginRequest;
import com.springonly.backend.model.request.RelationshipManagerUpdateRequest;
import com.springonly.backend.model.response.RelationshipManagerLoginResponse;
import com.springonly.backend.model.response.RelationshipManagerResponse;
import com.springonly.backend.model.dto.RelationshipManagerDto;

import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Path("/relationship-managers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RelationshipManagerResource {

    @Inject
    RelationshipManagerService relationshipManagerService;

    @Inject
    RelationshipManagerMapper relationshipManagerMapper;

    @HeaderParam("X-RelationshipManager-Id")
    String headerRelationshipManagerId;

    @POST
    @Path("/login")
    public Response loginRelationshipManager(RelationshipManagerLoginRequest request) {
        RelationshipManagerDto dto = relationshipManagerMapper.loginRequestToDto(request);
        Optional<RelationshipManagerDto> logged = relationshipManagerService.login(dto);

        if (logged.isEmpty()) {
            Map<String, String> errorResponse = Map.of("message", "Usuario y/o password incorrectos");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(errorResponse)
                    .build();
        }

        RelationshipManagerLoginResponse resp = relationshipManagerMapper.toLoginResponse(logged.get());
        return Response.ok(resp).build();
    }


    @GET
    @Path("/{relationshipManagerId}")
    public Response getRelationshipManagerById(@PathParam("relationshipManagerId") String relationshipManagerId) {
        Optional<RelationshipManagerDto> found = relationshipManagerService.getById(relationshipManagerId);
        if (found.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        RelationshipManagerResponse resp = relationshipManagerMapper.toResponse(found.get());
        return Response.ok(resp).build();
    }

    @PATCH
    @Path("/{relationshipManagerId}")
    @Transactional
    public Response updateRelationshipManager(@PathParam("relationshipManagerId") String relationshipManagerId, RelationshipManagerUpdateRequest request) {
        RelationshipManagerDto dto = relationshipManagerMapper.loginRequestToDto(new RelationshipManagerLoginRequest(relationshipManagerId, null));
        // apply updates
        relationshipManagerMapper.updateDtoFromRequest(request, dto);
        RelationshipManagerDto updated = relationshipManagerService.update(relationshipManagerId, dto);
        RelationshipManagerResponse resp = relationshipManagerMapper.toResponse(updated);
        return Response.ok(resp).build();
    }

    @GET
    @Path("/{relationshipManagerId}/customers")
    public Response listCustomersByRelationshipManagerById(@PathParam("relationshipManagerId") String relationshipManagerId) {
        // delegates to customer service via CDI lookup
        jakarta.enterprise.inject.Instance<com.springonly.backend.service.CustomerService> cs = jakarta.enterprise.inject.spi.CDI.current().select(com.springonly.backend.service.CustomerService.class);
        if (cs.isUnsatisfied()) return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        List<com.springonly.backend.model.dto.CustomerDto> list = cs.get().listByRelationshipManagerId(relationshipManagerId);
        List<com.springonly.backend.model.response.CustomerResponse> out = list.stream().map(d -> jakarta.enterprise.inject.spi.CDI.current().select(com.springonly.backend.mapper.CustomerMapper.class).get().toResponse(d)).collect(Collectors.toList());
        return Response.ok(out).build();
    }
}
