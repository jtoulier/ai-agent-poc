package com.springonly.backend.resource;

import com.springonly.backend.mapper.PaymentMapper;
import com.springonly.backend.model.dto.PaymentDTO;
import com.springonly.backend.model.request.CreatePaymentRequest;
import com.springonly.backend.model.request.UpdatePaymentRequest;
import com.springonly.backend.model.response.CreatePaymentResponse;
import com.springonly.backend.model.response.GetPaymentByIdResponse;
import com.springonly.backend.model.response.UpdatePaymentResponse;
import com.springonly.backend.model.response.generic.ErrorResponse;
import com.springonly.backend.service.PaymentService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.Operation;

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
    @Operation(operationId = "createPayment", summary = "Create payment", description = "Create a payment for a specific loan and payment number.")
    @APIResponses({
        @APIResponse(
            responseCode = "201",
            description = "Payment created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreatePaymentResponse.class)
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
    @Operation(operationId = "updatePayment", summary = "Update payment", description = "Update an existing payment for a loan identified by loanId and paymentNumber.")
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Payment updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UpdatePaymentResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Payment not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
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
    @Operation(operationId = "listPaymentsByLoanId", summary = "List payments", description = "List all payments associated to a given loan.")
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Payments list for loan",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = SchemaType.ARRAY, implementation = GetPaymentByIdResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "No payments found for loan",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
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
    @Operation(operationId = "getPaymentById", summary = "Get payment by id", description = "Retrieve a specific payment for a loan by loanId and paymentNumber.")
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Payment retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GetPaymentByIdResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Payment not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
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
