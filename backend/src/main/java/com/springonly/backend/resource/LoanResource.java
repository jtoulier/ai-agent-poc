package com.springonly.backend.resource;

import com.springonly.backend.model.response.PaymentResponse;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.springonly.backend.service.LoanService;
import com.springonly.backend.service.PaymentService;
import com.springonly.backend.mapper.LoanMapper;
import com.springonly.backend.mapper.PaymentMapper;
import com.springonly.backend.model.request.LoanRequest;
import com.springonly.backend.model.request.PaymentRequest;
import com.springonly.backend.model.response.LoanResponse;
import com.springonly.backend.model.dto.LoanDto;
import com.springonly.backend.model.dto.PaymentDto;

import java.util.List;

@Path("/loans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoanResource {

    @Inject
    LoanService loanService;

    @Inject
    PaymentService paymentService;

    @Inject
    LoanMapper loanMapper;

    @Inject
    PaymentMapper paymentMapper;

    @HeaderParam("X-RelationshipManager-Id")
    String headerRelationshipManagerId;

    @POST
    @Transactional
    public Response createLoan(
        LoanRequest request
    ) {
        LoanDto dto = loanMapper.requestToDto(request);
        LoanDto created = loanService.create(dto);
        LoanResponse resp = loanMapper.toResponse(created);
        return Response.status(Response.Status.CREATED).entity(resp).build();
    }

    @PATCH
    @Path("/{loanId}")
    @Transactional
    public Response updateLoan(
        @PathParam("loanId") Integer loanId,
        LoanRequest request
    ) {
        LoanDto dto = loanMapper.requestToDto(request);
        LoanDto updated = loanService.update(loanId, dto);
        LoanResponse resp = loanMapper.toResponse(updated);
        return Response.ok(resp).build();
    }

    @GET
    @Path("/{loanId}")
    public Response getLoanById(
        @PathParam("loanId") Integer loanId
    ) {
        LoanDto dto = loanService.getById(loanId);
        if (dto == null) return Response.status(Response.Status.NOT_FOUND).build();
        LoanResponse resp = loanMapper.toResponse(dto);
        return Response.ok(resp).build();
    }

    @POST
    @Path("/{loanId}/payments/{paymentNumber}")
    @Transactional
    public Response createPayment(
        @PathParam("loanId") Integer loanId,
        @PathParam("paymentNumber") Short paymentNumber,
        PaymentRequest request
    ) {
        PaymentDto dto = paymentMapper.requestToDto(request);
        dto.setLoanId(loanId);
        dto.setPaymentNumber(paymentNumber);
        PaymentDto created = paymentService.create(dto);
        PaymentResponse resp = paymentMapper.toResponse(created);
        return Response.status(Response.Status.CREATED).entity(resp).build();
    }

    @PATCH
    @Path("/{loanId}/payments/{paymentNumber}")
    @Transactional
    public Response updatePayment(
        @PathParam("loanId") Integer loanId,
        @PathParam("paymentNumber") Short paymentNumber,
        PaymentRequest request
    ) {
        PaymentDto dto = paymentMapper.requestToDto(request);
        dto.setLoanId(loanId);
        dto.setPaymentNumber(paymentNumber);
        PaymentDto updated = paymentService.update(loanId, paymentNumber, dto);
        PaymentResponse resp = paymentMapper.toResponse(updated);
        return Response.ok(resp).build();
    }

    @GET
    @Path("/{loanId}/payments")
    public Response listPaymentsByLoanId(@PathParam("loanId") Integer loanId) {
        List<PaymentDto> list = paymentService.listByLoanId(loanId);
        List<PaymentResponse> out = list.stream().map(d -> paymentMapper.toResponse(d)).toList();
        return Response.ok(out).build();
    }

    @GET
    @Path("/{loanId}/payments/{paymentNumber}")
    public Response getPaymentById(@PathParam("loanId") Integer loanId, @PathParam("paymentNumber") Short paymentNumber) {
        PaymentDto dto = paymentService.getById(loanId, paymentNumber);
        if (dto == null) return Response.status(Response.Status.NOT_FOUND).build();
        PaymentResponse resp = paymentMapper.toResponse(dto);
        return Response.ok(resp).build();
    }
}
