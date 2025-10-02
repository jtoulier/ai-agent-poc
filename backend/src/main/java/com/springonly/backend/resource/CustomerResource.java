package com.springonly.backend.resource;

import com.springonly.backend.model.request.CustomerRequest;
import com.springonly.backend.model.response.CustomerResponse;
import com.springonly.backend.model.dto.CustomerDTO;
import com.springonly.backend.mapper.CustomerMapper;
import com.springonly.backend.service.CustomerService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;
import java.util.stream.Collectors;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @Inject
    CustomerService service;

    @Inject
    CustomerMapper mapper;

    @GET
    public List<CustomerResponse> list() {
        return service.listAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") String id) {
        CustomerDTO dto = service.getById(id);
        if (dto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(mapper.toResponse(dto)).build();
    }

    @POST
    @Transactional
    public Response create(CustomerRequest req) {
        CustomerDTO dto = mapper.fromRequest(req);
        CustomerDTO saved = service.create(dto);
        return Response.status(Response.Status.CREATED)
                .entity(mapper.toResponse(saved))
                .build();
    }

    @PATCH
    @Transactional
    @Path("{id}")
    public Response patch(@PathParam("id") String id, CustomerRequest req) {
        CustomerDTO dto = mapper.fromRequest(req);
        dto.setCustomerId(id);
        CustomerDTO updated = service.patch(dto);
        return Response.ok(mapper.toResponse(updated)).build();
    }
}
