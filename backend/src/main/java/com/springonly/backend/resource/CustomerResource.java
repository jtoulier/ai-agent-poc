package com.springonly.backend.resource;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.springonly.backend.service.CustomerService;
import com.springonly.backend.mapper.CustomerMapper;
import com.springonly.backend.model.request.CustomerRequest;
import com.springonly.backend.model.response.CustomerResponse;
import com.springonly.backend.model.dto.CustomerDto;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @Inject
    CustomerService customerService;

    @Inject
    CustomerMapper customerMapper;

    @HeaderParam("X-RelationshipManager-Id")
    String headerRelationshipManagerId;

    @POST
    @Transactional
    public Response createCustomer(CustomerRequest request) {
        CustomerDto dto = customerMapper.requestToDto(request);
        CustomerDto created = customerService.create(dto);
        CustomerResponse resp = customerMapper.toResponse(created);
        return Response.status(Response.Status.CREATED).entity(resp).build();
    }

    @PATCH
    @Path("/{customerId}")
    @Transactional
    public Response updateCustomer(@PathParam("customerId") String customerId, CustomerRequest request) {
        CustomerDto dto = customerMapper.requestToDto(request);
        CustomerDto updated = customerService.update(customerId, dto);
        CustomerResponse resp = customerMapper.toResponse(updated);
        return Response.ok(resp).build();
    }

    @GET
    @Path("/{customerId}")
    public Response getCustomerById(@PathParam("customerId") String customerId) {
        CustomerDto dto = customerService.getById(customerId);
        if (dto == null) return Response.status(Response.Status.NOT_FOUND).build();
        CustomerResponse resp = customerMapper.toResponse(dto);
        return Response.ok(resp).build();
    }

    @GET
    @Path("/{customerId}/loans")
    public Response listLoansByCustomerId(@PathParam("customerId") String customerId) {
        java.util.List<com.springonly.backend.model.dto.LoanDto> list = jakarta.enterprise.inject.spi.CDI.current().select(com.springonly.backend.service.LoanService.class).get().listByCustomerId(customerId);
        java.util.List<com.springonly.backend.model.response.LoanResponse> out = list.stream().map(d -> jakarta.enterprise.inject.spi.CDI.current().select(com.springonly.backend.mapper.LoanMapper.class).get().toResponse(d)).toList();
        return Response.ok(out).build();
    }
}
