package com.springonly.backend.resource;

import com.springonly.backend.model.request.PaymentRequest;
import com.springonly.backend.model.response.PaymentResponse;
import com.springonly.backend.model.dto.PaymentDTO;
import com.springonly.backend.mapper.PaymentMapper;
import com.springonly.backend.service.PaymentService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {

    @Inject
    PaymentService service;

    @Inject
    PaymentMapper mapper;

    @GET
    public List<PaymentResponse> list() {
        return service.listAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @GET
    @Path("{loanId}/{paymentNumber}")
    public Response get(@PathParam("loanId") Integer loanId,
                        @PathParam("paymentNumber") Short paymentNumber) {
        PaymentDTO dto = service.getById(loanId, paymentNumber);
        if (dto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(mapper.toResponse(dto)).build();
    }

    @POST
    @Transactional
    @Path("{loanId}/{paymentNumber}")
    public Response create(@PathParam("loanId") Integer loanId,
                           @PathParam("paymentNumber") Short paymentNumber,
                           PaymentRequest req) {
        PaymentDTO dto = mapper.fromRequest(req);
        dto.setLoanId(loanId);
        dto.setPaymentNumber(paymentNumber);
        PaymentDTO saved = service.create(dto);
        return Response.status(Response.Status.CREATED)
                .entity(mapper.toResponse(saved))
                .build();
    }

    @PATCH
    @Transactional
    @Path("{loanId}/{paymentNumber}")
    public Response patch(@PathParam("loanId") Integer loanId,
                          @PathParam("paymentNumber") Short paymentNumber,
                          PaymentRequest req) {
        PaymentDTO dto = mapper.fromRequest(req);
        dto.setLoanId(loanId);
        dto.setPaymentNumber(paymentNumber);
        PaymentDTO updated = service.patch(dto);
        return Response.ok(mapper.toResponse(updated)).build();
    }
}
