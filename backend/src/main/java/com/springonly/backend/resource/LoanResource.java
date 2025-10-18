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
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.Optional;

@Path("/loans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoanResource {
    @Inject
    LoanService loanService;
    
    @Inject
    LoanMapper loanMapper;
    
    @HeaderParam("X-RelationshipManager-Id")
    String headerRelationshipManagerId;
    
    @POST
    @Path("/")
    @Transactional
    @Operation(operationId = "createLoan", summary = "Create loan", description = "Create a new loan for a customer and return the created loan information.")
    @APIResponses({
        @APIResponse(
            responseCode = "201",
            description = "Loan created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreateLoanResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "400",
            description = "Invalid request",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
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
    @Operation(operationId = "updateLoan", summary = "Update loan", description = "Update an existing loan's details identified by loanId.")
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Loan updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UpdateLoanResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "400",
            description = "Invalid request",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Loan not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
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
    @Operation(operationId = "getLoanById", summary = "Get loan by id", description = "Retrieve detailed information for a loan identified by loanId.")
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Loan retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GetLoanByIdResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Loan not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
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
