package com.springonly.backend.resource;

import com.springonly.backend.mapper.LoanMapper;
import com.springonly.backend.model.dto.LoanDTO;
import com.springonly.backend.model.request.CreateLoanRequest;
import com.springonly.backend.model.request.UpdateLoanRequest;
import com.springonly.backend.model.response.generic.ErrorResponse;
import com.springonly.backend.service.LoanService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
