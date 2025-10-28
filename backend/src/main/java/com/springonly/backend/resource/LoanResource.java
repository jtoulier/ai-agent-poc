package com.springonly.backend.resource;

import com.springonly.backend.mapper.LoanMapper;
import com.springonly.backend.model.dto.LoanDTO;
import com.springonly.backend.model.request.CreateLoanRequest;
import com.springonly.backend.model.request.UpdateLoanRequest;
import com.springonly.backend.model.response.CreateLoanResponse;
import com.springonly.backend.model.response.GetLoanByIdResponse;
import com.springonly.backend.model.response.UpdateLoanResponse;
import com.springonly.backend.model.response.generic.ErrorResponse;
import com.springonly.backend.service.LoanService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.Optional;

@Schema(
    name = "LoanResource",
    description = """
        Handles loan-related operations such as:
          - Creating new loans for customers
          - Updating existing loans
          - Retrieving loan details by ID
        """
)
@Path("/loans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoanResource {
    @Inject
    LoanService loanService;
    
    @Inject
    LoanMapper loanMapper;
    
    @HeaderParam("X-Relationship-Manager-Id")
    @Parameter(hidden = true)
    String headerRelationshipManagerId;
    
    @POST
    @Path("/")
    @Transactional
    @Operation(
        operationId = "createLoan",
        summary = "Create a new loan",
        description = "Creates a loan for a customer. Requires a CreateLoanRequest payload with the necessary loan data."
    )
    @RequestBody(
        description = "CreateLoanRequest payload",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = CreateLoanRequest.class),
            examples = {
                @ExampleObject(
                    name = "Create loan example",
                    summary = "Request to create a loan",
                    value = """
                    {
                      "customerId": "CUST12345",
                      "currencyId": "PEN",
                      "principalAmount": 10000.00,
                      "interestRate": 5.5,
                      "loanDisbursementDate": "2025-09-01",
                      "numberOfMonthlyPayments": 36
                    }
                    """
                )
            }
        )
    )
    @APIResponses({
        @APIResponse(
            responseCode = "201",
            description = "Loan created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreateLoanResponse.class),
                examples = {
                    @ExampleObject(
                        name = "CreateLoanResponse example",
                        summary = "Created loan representation",
                        value = """
                        {
                          "loanId": 12345,
                          "customerId": "CUST12345",
                          "currencyId": "PEN",
                          "principalAmount": 10000.00,
                          "interestRate": 5.5,
                          "loanDisbursementDate": "2025-09-01",
                          "numberOfMonthlyPayments": 36,
                          "loanStateId": "EN EVALUACION",
                          "writtenAt": "2025-10-07T22:57:09-05:00"
                        }
                        """
                    )
                }
            )
        ),
        @APIResponse(
            responseCode = "400",
            description = "Invalid request",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "CreateLoan error",
                        summary = "Invalid input example",
                        value = """
                        {
                          "message": "principalAmount must be greater than 0",
                          "code": "INVALID_REQUEST"
                        }
                        """
                    )
                }
            )
        )
    })
    public Response createLoan(
        CreateLoanRequest request
    ) {
        // 1️⃣ Convertimos el request a DTO
        LoanDTO fromRequestToDTO = loanMapper.fromCreateRequestToDTO(request);
        
        // 2️⃣ Llamamos al servicio para crear el préstamo
        LoanDTO createdLoan = loanService.createLoan(fromRequestToDTO);
        
        // 3️⃣ Retornamos la respuesta con código 201 (CREATED)
        return Response
                .status(Response.Status.CREATED)
                .entity(loanMapper.fromDTOToCreateResponse(createdLoan))
                .build();
    }
    
    @PATCH
    @Path("/{loanId}")
    @Transactional
    @Operation(
        operationId = "updateLoan",
        summary = "Update an existing loan",
        description = "Updates loan fields for the given loanId using UpdateLoanRequest."
    )
    @RequestBody(
        description = "UpdateLoanRequest payload",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UpdateLoanRequest.class),
            examples = {
                @ExampleObject(
                    name = "Update loan example",
                    summary = "Request to update a loan state",
                    value = """
                    {
                      "loanStateId": "VIGENTE"
                    }
                    """
                )
            }
        )
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Loan updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UpdateLoanResponse.class),
                examples = {
                    @ExampleObject(
                        name = "UpdateLoanResponse example",
                        summary = "Updated loan representation",
                        value = """
                        {
                          "loanId": 12345,
                          "customerId": "CUST12345",
                          "currencyId": "USD",
                          "principalAmount": 10000.00,
                          "interestRate": 5.5,
                          "loanDisbursementDate": "2025-09-01",
                          "numberOfMonthlyPayments": 36,
                          "loanStateId": "VIGENTE",
                          "writtenAt": "2025-10-07T23:05:32-05:00"
                        }
                        """
                    )
                }
            )
        ),
        @APIResponse(
            responseCode = "400",
            description = "Invalid request",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "UpdateLoan error",
                        summary = "Invalid update example",
                        value = """
                        {
                          "message": "Invalid update data",
                          "code": "INVALID_UPDATE"
                        }
                        """
                    )
                }
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Loan not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "UpdateLoan not found",
                        summary = "Loan to update was not found",
                        value = """
                        {
                          "message": "El préstamo indicado no existe",
                          "code": "LN002"
                        }
                        """
                    )
                }
            )
        )
    })
    public Response updateLoan(
        @PathParam("loanId") Integer loanId,
        UpdateLoanRequest request
    ) {
        // 1️⃣ Convertimos el request a DTO
        LoanDTO fromRequestToDTO = loanMapper.fromUpdateRequestToDTO(request);
        fromRequestToDTO.setLoanId(loanId);
        
        // 2️⃣ Intentamos actualizar el préstamo
        Optional<LoanDTO> updated = loanService.updateLoan(fromRequestToDTO);
        
        // 3️⃣ Si no existe, retornamos 404
        if (updated.isEmpty()) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(
                    "El préstamo indicado no existe",
                    "LN002"
                ))
                .build();
        }
        
        // 4️⃣ Si se actualiza, retornamos 200 con la respuesta
        return Response
            .ok(
                loanMapper.fromDTOToUpdateResponse(updated.get())
            )
            .build();
    }
    
    @GET
    @Path("/{loanId}")
    @Operation(
        operationId = "getLoanById",
        summary = "Get loan by ID",
        description = "Retrieves the loan details for the provided loanId."
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Loan retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GetLoanByIdResponse.class),
                examples = {
                    @ExampleObject(
                        name = "GetLoanByIdResponse example",
                        summary = "A loan retrieved by id",
                        value = """
                        {
                          "loanId": 12345,
                          "customerId": "CUST12345",
                          "currencyId": "PEN",
                          "principalAmount": 10000.00,
                          "interestRate": 5.5,
                          "loanDisbursementDate": "2025-09-01",
                          "numberOfMonthlyPayments": 36,
                          "loanStateId": "VIGENTE",
                          "writtenAt": "2025-09-16T15:45:00-05:00"
                        }
                        """
                    )
                }
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Loan not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "GetLoanById error",
                        summary = "Loan not found example",
                        value = """
                        {
                          "message": "El préstamo indicado no existe",
                          "code": "LN003"
                        }
                        """
                    )
                }
            )
        )
    })
    public Response getLoanById(
        @PathParam("loanId") Integer loanId
    ) {
        // 1️⃣ Buscamos el préstamo por ID
        Optional<LoanDTO> loanOpt = loanService.getLoanById(loanId);
        
        // 2️⃣ Si no se encuentra, devolvemos 404
        if (loanOpt.isEmpty()) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(
                    new ErrorResponse(
                        "El préstamo indicado no existe",
                        "LN003"
                    )
                )
                .build();
        }
        
        // 3️⃣ Si existe, devolvemos el detalle
        return Response
            .ok(loanMapper.fromDTOToGetByIdResponse(loanOpt.get()))
            .build();
    }
}
