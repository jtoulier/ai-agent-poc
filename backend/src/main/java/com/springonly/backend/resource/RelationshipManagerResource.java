package com.springonly.backend.resource;

import com.springonly.backend.mapper.CustomerMapper;
import com.springonly.backend.mapper.RelationshipManagerMapper;
import com.springonly.backend.model.dto.CustomerDTO;
import com.springonly.backend.model.dto.RelationshipManagerDTO;
import com.springonly.backend.model.request.LoginRelationshipManagerRequest;
import com.springonly.backend.model.request.UpdateRelationshipManagerThreadIdRequest;
import com.springonly.backend.model.response.GetCustomerByIdResponse;
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

import com.springonly.backend.model.response.LoginRelationshipManagerResponse;
import com.springonly.backend.model.response.UpdateRelationshipManagerThreadIdResponse;
import com.springonly.backend.model.response.GetRelationshipManagerByIdResponse;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;

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
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Login successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginRelationshipManagerResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public Response loginRelationshipManager(
        LoginRelationshipManagerRequest request
    ) {
        RelationshipManagerDTO fromRequestToDTO = relationshipManagerMapper.fromLoginRequestToDTO(request);

        return relationshipManagerService.loginRelationshipManager(fromRequestToDTO)
            .map(
                dtoFromService
                ->
                Response
                .ok(relationshipManagerMapper.fromDTOToLoginResponse(dtoFromService))
                .build()
            )
            .orElseGet(
                () ->
                Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(
                    new ErrorResponse(
                        "Usuario y/o password incorrectos",
                        "RM001")
                )
                .build()
            );
    }

    @PATCH
    @Path("/{relationshipManagerId}")
    @Transactional
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Relationship manager thread id updated",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UpdateRelationshipManagerThreadIdResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Relationship manager not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public Response updateRelationshipManagerThreadId(
        @PathParam("relationshipManagerId") String relationshipManagerId,
        UpdateRelationshipManagerThreadIdRequest request
    ) {
        RelationshipManagerDTO fromRequestToDTO = relationshipManagerMapper.fromUpdateThreadIdRequestToDTO(request);
        fromRequestToDTO.setRelationshipManagerId(relationshipManagerId);

        return relationshipManagerService
                .updateRelationshipManagerThreadId(fromRequestToDTO)
                .map(
                    dtoFromService
                    ->
                    Response.ok(relationshipManagerMapper.fromDTOToUpdateThreadIdResponse(dtoFromService))
                    .build()
                )
                .orElseGet(
                    ()
                    ->
                    Response.status(Response.Status.NOT_FOUND)
                    .entity(
                        new ErrorResponse(
                            "El Ejecutivo de Cuenta indicado no existe",
                            "RM002"
                        )
                    )
                    .build()
                );
    }

    @GET
    @Path("/{relationshipManagerId}")
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Relationship manager retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GetRelationshipManagerByIdResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Relationship manager not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public Response getRelationshipManagerById(
        @PathParam("relationshipManagerId") String relationshipManagerId
    ) {
        return relationshipManagerService
                .getRelationshipManagerById(relationshipManagerId)
                .map(
                    dtoFromService ->
                    Response
                    .ok(relationshipManagerMapper.fromDTOToGetByIdResponse(dtoFromService))
                    .build()
                )
                .orElseGet(
                    ()
                    ->
                    Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(
                        new ErrorResponse(
                            "El Ejecutivo de Cuenta indicado no existe",
                            "RM003")
                    )
                    .build()
                );
    }
    
    @GET
    @Path("/{relationshipManagerId}/customers")
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Customers list for relationship manager",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = SchemaType.ARRAY, implementation = GetCustomerByIdResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "No customers found for relationship manager",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public Response listCustomersByRelationshipManagerById(
        @PathParam("relationshipManagerId") String relationshipManagerId
    ) {
        List<CustomerDTO> dtos =
            customerService.listCustomersByRelationshipManagerById(relationshipManagerId);
        
        if (dtos.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(
                        new ErrorResponse(
                            "El Ejecutivo de Cuentas no tiene clientes asignados",
                            "RM002"
                        )
                    )
                    .build();
        }
        
        // Mapeamos y ordenamos los resultados
        List<GetCustomerByIdResponse> responses = dtos.stream()
            .map(customerMapper::fromDTOToGetByIdResponse)
            .sorted(Comparator.comparing(GetCustomerByIdResponse::getCustomerName))
            .toList();
        
        return Response.ok(responses).build();
    }
}