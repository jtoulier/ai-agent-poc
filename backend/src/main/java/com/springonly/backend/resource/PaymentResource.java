package com.springonly.backend.resource;

import com.springonly.backend.mapper.PaymentMapper;
import com.springonly.backend.model.dto.PaymentDTO;
import com.springonly.backend.model.request.CreatePaymentRequest;
import com.springonly.backend.model.request.UpdatePaymentRequest;
import com.springonly.backend.model.response.GetPaymentByIdResponse;
import com.springonly.backend.model.response.generic.ErrorResponse;
import com.springonly.backend.service.PaymentService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Comparator;
import java.util.List;

@Path("/loans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {
    @Inject
    PaymentService paymentService;
    
    @Inject
    PaymentMapper paymentMapper;
    
    @HeaderParam("X-RelationshipManager-Id")
    String headerRelationshipManagerId;
    
    // =======================================================
    // POST: Crear un pago
    // =======================================================
    @POST
    @Path("/{loanId}/payments/{paymentNumber}")
    @Transactional
    public Response createPayment(
        @PathParam("loanId") Integer loanId,
        @PathParam("paymentNumber") Short paymentNumber,
        CreatePaymentRequest request
    ) {
        PaymentDTO dto = paymentMapper.fromCreateRequestToDTO(request);
        dto.setLoanId(loanId);
        dto.setPaymentNumber(paymentNumber);
        
        PaymentDTO created = paymentService.createPayment(dto);
        
        return Response
                .status(Response.Status.CREATED)
                .entity(paymentMapper.fromDTOToCreateResponse(created))
                .build();
    }
    
    // =======================================================
    // PATCH: Actualizar un pago
    // =======================================================
    @PATCH
    @Path("/{loanId}/payments/{paymentNumber}")
    @Transactional
    public Response updatePayment(
        @PathParam("loanId") Integer loanId,
        @PathParam("paymentNumber") Short paymentNumber,
        UpdatePaymentRequest request
    ) {
        PaymentDTO dto = paymentMapper.fromUpdateRequestToDTO(request);
        dto.setLoanId(loanId);
        dto.setPaymentNumber(paymentNumber);
        
        return paymentService.updatePayment(dto)
            .map(updated ->
                Response.ok(paymentMapper.fromDTOToUpdateResponse(updated)).build()
            )
            .orElseGet(() ->
                Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(
                        "El pago indicado no existe para este préstamo",
                        "PAY404")
                    )
                    .build()
            );
    }
    
    // =======================================================
    // GET: Listar pagos por loanId
    // =======================================================
    @GET
    @Path("/{loanId}/payments")
    public Response listPaymentsByLoanId(
        @PathParam("loanId") Integer loanId
    ) {
        List<PaymentDTO> dtos = paymentService.listPaymentsByLoanId(loanId);
        
        if (dtos.isEmpty()) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(
                    "El préstamo no tiene pagos registrados",
                    "PAY002"))
                .build();
        }
        
        List<GetPaymentByIdResponse> responses = dtos.stream()
            .map(paymentMapper::fromDTOToGetByIdResponse)
            .sorted(Comparator.comparing(GetPaymentByIdResponse::getPaymentNumber))
            .toList();
        
        return Response.ok(responses).build();
    }
    
    // =======================================================
    // GET: Obtener un pago específico
    // =======================================================
    @GET
    @Path("/{loanId}/payments/{paymentNumber}")
    public Response getPaymentById(
        @PathParam("loanId") Integer loanId,
        @PathParam("paymentNumber") Short paymentNumber
    ) {
        return paymentService.getPaymentById(loanId, paymentNumber)
            .map(dto ->
                Response.ok(paymentMapper.fromDTOToGetByIdResponse(dto)).build()
            )
            .orElseGet(() ->
                Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(
                        "El pago solicitado no existe",
                        "PAY003"))
                    .build()
            );
    }
}
