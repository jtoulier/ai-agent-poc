package com.springonly.backend.resource;

import com.springonly.backend.mapper.CustomerMapper;
import com.springonly.backend.mapper.RelationshipManagerMapper;
import com.springonly.backend.model.dto.CustomerDTO;
import com.springonly.backend.model.dto.RelationshipManagerDTO;
import com.springonly.backend.model.request.LoginRelationshipManagerRequest;
import com.springonly.backend.model.request.UpdateRelationshipManagerThreadIdRequest;
import com.springonly.backend.model.response.GetCustomerByIdResponse;
import com.springonly.backend.model.response.GetRelationshipManagerByIdResponse;
import com.springonly.backend.model.response.LoginRelationshipManagerResponse;
import com.springonly.backend.model.response.UpdateRelationshipManagerThreadIdResponse;
import com.springonly.backend.model.response.generic.ErrorResponse;
import com.springonly.backend.service.CustomerService;
import com.springonly.backend.service.RelationshipManagerService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.Comparator;
import java.util.List;

@Schema(
    name = "RelationshipManagerResource",
    description = """
        Handles relationship-manager related operations such as:
          - Authenticating relationship managers
          - Updating thread id information
          - Retrieving relationship manager details
          - Listing customers assigned to a relationship manager
        """
)
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

    @HeaderParam("X-Relationship-Manager-Id")
    @Parameter(hidden = true)
    String headerRelationshipManagerId;

    @POST
    @Path("/login")
    @Operation(operationId = "loginRelationshipManager", summary = "Login relationship manager", description = "Authenticate a relationship manager and return session or token details.")
    @RequestBody(
        description = "LoginRelationshipManagerRequest payload",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = LoginRelationshipManagerRequest.class),
            examples = {
                @ExampleObject(
                    name = "Login request",
                    summary = "Request to authenticate a relationship manager",
                    value = """
                    {
                      "relationshipManagerId": "RM001",
                      "password": "s3cr3t"
                    }
                    """
                )
            }
        )
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Login successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginRelationshipManagerResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Login successful",
                        summary = "Authenticated relationship manager response",
                        value = """
                        {
                          "relationshipManagerId": "RM001",
                          "relationshipManagerName": "Ana Pérez",
                          "threadId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                          "writtenAt": "2025-10-07T22:27:36-05:00"
                        }
                        """
                    )
                }
            )
        ),
        @APIResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Unauthorized login",
                        summary = "Invalid credentials example",
                        value = """
                        {
                          "message": "Usuario y/o password incorrectos",
                          "code": "RM001"
                        }
                        """
                    )
                }
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
    @Operation(
        operationId = "updateRelationshipManagerThreadId",
        summary = "Update thread id",
        description = "Update the thread id information for a relationship manager."
    )
    @RequestBody(
        description = "UpdateRelationshipManagerThreadIdRequest payload",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UpdateRelationshipManagerThreadIdRequest.class),
            examples = {
                @ExampleObject(
                    name = "Update thread id request",
                    summary = "Request to update relationship manager thread id",
                    value = """
                    {
                      "threadId": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
                    }
                    """
                )
            }
        )
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Relationship manager thread id updated",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UpdateRelationshipManagerThreadIdResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Update thread id success",
                        summary = "Updated relationship manager thread id response",
                        value = """
                        {
                          "relationshipManagerId": "RM001",
                          "relationshipManagerName": "Ana Pérez",
                          "threadId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                          "writtenAt": "2025-10-07T22:27:36-05:00"
                        }
                        """
                    )
                }
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Relationship manager not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Update thread id not found",
                        summary = "Relationship manager not found example",
                        value = """
                        {
                          "message": "El Ejecutivo de Cuenta indicado no existe",
                          "code": "RM002"
                        }
                        """
                    )
                }
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
    @Operation(operationId = "getRelationshipManagerById", summary = "Get relationship manager", description = "Retrieve relationship manager details by id.")
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Relationship manager retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GetRelationshipManagerByIdResponse.class),
                examples = {
                    @ExampleObject(
                        name = "GetRelationshipManagerByIdResponse example",
                        summary = "A relationship manager retrieved by id",
                        value = """
                        {
                          "relationshipManagerId": "RM001",
                          "relationshipManagerName": "Ana Pérez",
                          "threadId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                          "writtenAt": "2025-10-07T22:27:36-05:00"
                        }
                        """
                    )
                }
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Relationship manager not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "GetRelationshipManager not found",
                        summary = "Requested relationship manager does not exist",
                        value = """
                        {
                          "message": "El Ejecutivo de Cuenta indicado no existe",
                          "code": "RM003"
                        }
                        """
                    )
                }
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
    @Operation(operationId = "listCustomersByRelationshipManagerById", summary = "List customers", description = "List customers assigned to a specific relationship manager.")
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Customers list for relationship manager",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = SchemaType.ARRAY, implementation = GetCustomerByIdResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Customers list",
                        summary = "A list of customers for a relationship manager",
                        value = """
                        [
                          {
                            "customerId": "CUST12345",
                            "customerName": "John Doe",
                            "customerTypeId": "MICRO EMPRESA",
                            "riskCategoryId": "DU",
                            "lineOfCreditAmount": 50000.00,
                            "relationshipManagerId": "RM001",
                            "writtenAt": "2024-12-14T19:37:26-05:00"
                          },
                          {
                            "customerId": "CUST67890",
                            "customerName": "María López",
                            "customerTypeId": "PERSONAL",
                            "riskCategoryId": "PP",
                            "lineOfCreditAmount": 15000.00,
                            "relationshipManagerId": "RM001",
                            "writtenAt": "2025-01-20T15:00:00-05:00"
                          }
                        ]
                        """
                    )
                }
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "No customers found for relationship manager",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "No customers found",
                        summary = "No customers for given relationship manager",
                        value = """
                        {
                          "message": "El Ejecutivo de Cuentas no tiene clientes asignados",
                          "code": "RM002"
                        }
                        """
                    )
                }
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