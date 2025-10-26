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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.Comparator;
import java.util.List;

@Schema(
    name = "PaymentResource",
    description = """
        Handles payment-related operations such as:
          - Creating payments for a loan
          - Updating existing payments
          - Listing payments for a specific loan
          - Retrieving a payment by loanId and paymentNumber
        """
)
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
    @RequestBody(
        description = "CreatePaymentRequest payload",
        required = true,
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = CreatePaymentRequest.class),
            examples = {
                @ExampleObject(
                    name = "CreatePayment request",
                    summary = "Request to create a payment",
                    value = """
                    {
                      "dueDate": "2025-10-15",
                      "principalAmount": 500.00,
                      "interestAmount": 5.00
                    }
                    """
                )
            }
        )
    )
    @APIResponses({
        @APIResponse(
            responseCode = "201",
            description = "Payment created successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = CreatePaymentResponse.class),
                examples = {
                    @ExampleObject(
                        name = "CreatePaymentResponse example",
                        summary = "Created payment representation",
                        value = """
                        {
                          "loanId": 12345,
                          "paymentNumber": 1,
                          "dueDate": "2025-10-15",
                          "principalAmount": 500.00,
                          "interestAmount": 5.00,
                          "totalPaymentAmount": 505.00,
                          "paymentStateId": "PENDIENTE",
                          "writtenAt": "2025-09-16T15:45:00-05:00"
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
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "CreatePayment error",
                        summary = "Invalid create payment input",
                        value = """
                        {
                          "message": "Invalid input data",
                          "code": "PAY400"
                        }
                        """
                    )
                }
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
    @RequestBody(
        description = "UpdatePaymentRequest payload",
        required = true,
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = UpdatePaymentRequest.class),
            examples = {
                @ExampleObject(
                    name = "UpdatePayment request",
                    summary = "Request to update payment state",
                    value = """
                    {
                      "paymentStateId": "PAGADO"
                    }
                    """
                )
            }
        )
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Payment updated successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = UpdatePaymentResponse.class),
                examples = {
                    @ExampleObject(
                        name = "UpdatePaymentResponse example",
                        summary = "Updated payment representation",
                        value = """
                        {
                          "loanId": 12345,
                          "paymentNumber": 1,
                          "dueDate": "2025-10-15",
                          "principalAmount": 500.00,
                          "interestAmount": 5.00,
                          "totalPaymentAmount": 505.00,
                          "paymentStateId": "PAGADO",
                          "writtenAt": "2025-09-16T15:45:00-05:00"
                        }
                        """
                    )
                }
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Payment not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "UpdatePayment not found",
                        summary = "Payment to update was not found",
                        value = """
                        {
                          "message": "El pago indicado no existe para este préstamo",
                          "code": "PAY404"
                        }
                        """
                    )
                }
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
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(type = SchemaType.ARRAY, implementation = GetPaymentByIdResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Payments list",
                        summary = "A list of payments for a loan",
                        value = """
                        [
                          {
                            "loanId": 12345,
                            "paymentNumber": 1,
                            "dueDate": "2025-10-15",
                            "principalAmount": 500.00,
                            "interestAmount": 5.00,
                            "totalPaymentAmount": 505.00,
                            "paymentStateId": "PAGADO",
                            "writtenAt": "2025-08-16T13:25:00-05:00"
                          },
                          {
                            "loanId": 12345,
                            "paymentNumber": 2,
                            "dueDate": "2025-11-15",
                            "principalAmount": 500.00,
                            "interestAmount": 4.50,
                            "totalPaymentAmount": 504.50,
                            "paymentStateId": "PENDIENTE",
                            "writtenAt": "2025-07-16T11:15:00-05:00"
                          }
                        ]
                        """
                    )
                }
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "No payments found for loan",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "No payments found",
                        summary = "No payments for given loan",
                        value = """
                        {
                          "message": "El préstamo no tiene pagos registrados",
                          "code": "PAY002"
                        }
                        """
                    )
                }
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
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = GetPaymentByIdResponse.class),
                examples = {
                    @ExampleObject(
                        name = "GetPaymentByIdResponse example",
                        summary = "A payment retrieved by id",
                        value = """
                        {
                          "loanId": 12345,
                          "paymentNumber": 1,
                          "dueDate": "2025-10-15",
                          "principalAmount": 500.00,
                          "interestAmount": 5.00,
                          "totalPaymentAmount": 505.00,
                          "paymentStateId": "PAGADO",
                          "writtenAt": "2025-09-16T15:45:00-05:00"
                        }
                        """
                    )
                }
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Payment not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "GetPaymentById not found",
                        summary = "Requested payment does not exist",
                        value = """
                        {
                          "message": "El pago solicitado no existe",
                          "code": "PAY003"
                        }
                        """
                    )
                }
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
