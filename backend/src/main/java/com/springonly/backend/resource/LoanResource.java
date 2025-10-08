package com.springonly.backend.resource;

import com.springonly.backend.mapper.LoanMapper;
import com.springonly.backend.model.dto.LoanDTO;
import com.springonly.backend.model.request.LoanRequest;
import com.springonly.backend.model.response.LoanResponse;
import com.springonly.backend.service.LoanService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/loans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoanResource {

    @Inject
    LoanService service;

    @Inject
    LoanMapper mapper;

    @GET
    public List<LoanResponse> list() {
        return service.listAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") Integer id) {
        LoanDTO dto = service.getById(id);
        if (dto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(mapper.toResponse(dto)).build();
    }

    @POST
    @Transactional
    public Response create(LoanRequest req) {
        LoanDTO dto = mapper.fromRequest(req);
        LoanDTO saved = service.create(dto);
        return Response.status(Response.Status.CREATED)
                .entity(mapper.toResponse(saved))
                .build();
    }

    @PATCH
    @Transactional
    @Path("{id}")
    public Response patch(@PathParam("id") Integer id, LoanRequest req) {
        LoanDTO dto = mapper.fromRequest(req);
        dto.setLoanId(id);
        LoanDTO updated = service.patch(dto);
        return Response.ok(mapper.toResponse(updated)).build();
    }
}
