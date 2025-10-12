package com.springonly.backend.resource;

import com.springonly.backend.mapper.CustomerMapper;
import com.springonly.backend.mapper.RelationshipManagerMapper;
import com.springonly.backend.model.dto.CustomerDTO;
import com.springonly.backend.model.dto.RelationshipManagerDTO;
import com.springonly.backend.model.request.LoginRelationshipManagerRequest;
import com.springonly.backend.model.request.UpdateRelationshipManagerThreadIdRequest;
import com.springonly.backend.model.response.GetCustomerByIdResponse;
import com.springonly.backend.model.response.generic.CustomerResponse;
import com.springonly.backend.model.response.generic.ErrorResponse;
import com.springonly.backend.service.CustomerService;
import com.springonly.backend.service.RelationshipManagerService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Path("/relationship-managers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RelationshipManagerResource {
    @Inject
    RelationshipManagerService relationshipManagerService;

    @Inject
    RelationshipManagerMapper relationshipManagerMapper;

    @Inject
    CustomerService customerService;

    @Inject
    CustomerMapper customerMapper;

    @HeaderParam("X-RelationshipManager-Id")
    String headerRelationshipManagerId;

    @POST
    @Path("/login")
    public Response loginRelationshipManager(
            LoginRelationshipManagerRequest request
    ) {
        RelationshipManagerDTO fromRequestToDTO = relationshipManagerMapper.fromLoginRequestToDTO(request);

        return relationshipManagerService.loginRelationshipManager(fromRequestToDTO)
                .map(
                        dtoFromService
                                -> Response.ok(relationshipManagerMapper.fromDTOToLoginResponse(dtoFromService)).build())
                .orElseGet(
                        () ->
                                Response.status(Response.Status.UNAUTHORIZED)
                                        .entity(new ErrorResponse("Usuario y/o password incorrectos", 401))
                                        .build()
                );
    }

    @PATCH
    @Path("/{relationshipManagerId}")
    @Transactional
    public Response updateRelationshipManagerThreadId(
            @PathParam("relationshipManagerId") String relationshipManagerId,
            UpdateRelationshipManagerThreadIdRequest request
    ) {
        RelationshipManagerDTO fromRequestToDTO = relationshipManagerMapper.fromUpdateThreadIdRequestToDTO(request);
        fromRequestToDTO.setRelationshipManagerId(relationshipManagerId);

        return relationshipManagerService
                .updateRelationshipManagerThreadId(fromRequestToDTO)
                .map(dtoFromService ->
                        Response.ok(relationshipManagerMapper.fromDTOToUpdateThreadIdResponse(dtoFromService)).build()
                )
                .orElseGet(
                        () ->
                                Response.status(Response.Status.NOT_FOUND)
                                        .entity(new ErrorResponse("Relationship Manager no encontrado", 404))
                                        .build()
                );
    }

    @GET
    @Path("/{relationshipManagerId}")
    public Response getRelationshipManagerById(
            @PathParam("relationshipManagerId") String relationshipManagerId
    ) {
        return relationshipManagerService
                .getRelationshipManagerById(relationshipManagerId)
                .map(dtoFromService ->
                        Response.ok(relationshipManagerMapper.fromDTOToGetByIdResponse(dtoFromService)).build()
                )
                .orElseGet(
                        () ->
                                Response.status(Response.Status.NOT_FOUND)
                                        .entity(new ErrorResponse("Relationship Manager no encontrado", 404))
                                        .build()
                );
    }

    @GET
    @Path("/{relationshipManagerId}/customers")
    public Response listCustomersByRelationshipManagerById(
        @PathParam("relationshipManagerId") String relationshipManagerId
    ) {
        Optional<List<CustomerDTO>> optionalDtos =
            customerService.listCustomersByRelationshipManagerById(relationshipManagerId);

        // Si el Optional está vacío, retornamos 404 o lista vacía según tu política
        List<GetCustomerByIdResponse> responses = optionalDtos
            .orElse(List.of()) // devuelve lista vacía si no hay resultados
            .stream()
            .map(customerMapper::fromDTOToGetByIdResponse)
            .sorted(Comparator.comparing(GetCustomerByIdResponse::getCustomerName))
            .toList();

        return Response.ok(responses).build();
    }
}