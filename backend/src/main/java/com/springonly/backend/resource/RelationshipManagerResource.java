package com.springonly.backend.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

import com.springonly.backend.service.RelationshipManagerService;
import com.springonly.backend.mapper.RelationshipManagerMapper;
import com.springonly.backend.mapper.CustomerMapper;
import com.springonly.backend.model.request.LoginRequest;
import com.springonly.backend.model.request.UpdateRelationshipManagerRequest;
import com.springonly.backend.model.response.RelationshipManagerLoginResponse;
import com.springonly.backend.model.response.RelationshipManagerResponse;
import com.springonly.backend.model.response.CustomerResponse;
import com.springonly.backend.model.dto.RelationshipManagerDTO;
import com.springonly.backend.model.dto.CustomerDTO;

@Path("/api/relationship-managers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RelationshipManagerResource {

    @Inject
    RelationshipManagerService relationshipManagerService;

    @Inject
    RelationshipManagerMapper relationshipManagerMapper;

    @Inject
    CustomerMapper customerMapper;

    @POST
    @Path("/login")
    public Response loginRelationshipManager(@Valid LoginRequest loginRequest) {
        RelationshipManagerDTO inputDto = relationshipManagerMapper.fromLoginRequestToDto(loginRequest);
        RelationshipManagerDTO resultDto = relationshipManagerService.loginRelationshipManager(inputDto);

        if (resultDto == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Credenciales inválidas\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        RelationshipManagerLoginResponse resp = relationshipManagerMapper.toLoginResponse(resultDto);
        return Response.ok(resp).build();
    }

    @PATCH
    @Path("/{relationshipManagerId}")
    public Response updateRelationshipManager(
            @PathParam("relationshipManagerId") String relationshipManagerId,
            @Valid UpdateRelationshipManagerRequest updateRequest) {

        RelationshipManagerDTO dtoToUpdate = relationshipManagerMapper.fromUpdateRequestToDto(updateRequest);
        dtoToUpdate.setRelationshipManagerId(relationshipManagerId);

        try {
            RelationshipManagerDTO updated = relationshipManagerService.updateRelationshipManager(dtoToUpdate);

            if (updated == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Ejecutivo de Cuenta no encontrado\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            RelationshipManagerResponse response = relationshipManagerMapper.toResponse(updated);
            return Response.ok(response).build();

        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + ex.getMessage() + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @GET
    @Path("/{relationshipManagerId}")
    public Response getRelationshipManagerById(@PathParam("relationshipManagerId") String relationshipManagerId) {
        RelationshipManagerDTO dto = relationshipManagerService.getRelationshipManagerById(relationshipManagerId);

        if (dto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Ejecutivo de Cuenta no encontrado\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        RelationshipManagerResponse response = relationshipManagerMapper.toResponse(dto);
        return Response.ok(response).build();
    }

    @GET
    @Path("/{relationshipManagerId}/customers")
    public Response listCustomersByRelationshipManagerById(@PathParam("relationshipManagerId") String relationshipManagerId) {
        List<CustomerDTO> customers = relationshipManagerService.listCustomersByRelationshipManagerId(relationshipManagerId);

        if (customers == null || customers.isEmpty()) {
            return Response.ok().entity(List.of()).build();
        }

        List<CustomerResponse> responses = customers.stream()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());

        return Response.ok(responses).build();
    }
}
