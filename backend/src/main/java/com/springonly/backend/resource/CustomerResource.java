package com.springonly.backend.resource;

import com.springonly.backend.mapper.CustomerMapper;
import com.springonly.backend.model.dto.CustomerDTO;
import com.springonly.backend.model.request.CreateCustomerRequest;
import com.springonly.backend.model.request.UpdateCustomerRequest;
import com.springonly.backend.model.response.CreateCustomerResponse;
import com.springonly.backend.model.response.GetCustomerByIdResponse;
import com.springonly.backend.model.response.UpdateCustomerResponse;
import com.springonly.backend.model.response.generic.ErrorResponse;
import com.springonly.backend.service.CustomerService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

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
    @Path("/")
    @Transactional
    public Response createCustomer(
        CreateCustomerRequest request
    ) {
        CustomerDTO fromRequestToDTO = customerMapper.fromCreateRequestToDTO(request);
        
        CustomerDTO createdCustomerDTO = customerService.createCustomer(fromRequestToDTO);
        
        CreateCustomerResponse response = customerMapper.fromDTOToCreateResponse(createdCustomerDTO);
        
        return Response
            .status(Response.Status.CREATED)
            .entity(response)
            .build();
    }
    
    @PATCH
    @Path("/{customerId}")
    @Transactional
    public Response updateCustomer(
        @PathParam("customerId") String customerId,
        UpdateCustomerRequest request
    ) {
        // 1️⃣. Convertimos el request a DTO
        CustomerDTO fromRequestToDTO = customerMapper.fromUpdateRequestToDTO(request);
        fromRequestToDTO.setCustomerId(customerId);
        
        // 2️⃣ Intentamos actualizar el cliente
        Optional<CustomerDTO> updatedOptional =
            customerService.updateCustomer(fromRequestToDTO);
        
        // 3️⃣ Si no existe el cliente, devolvemos 404
        if (updatedOptional.isEmpty()) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(
                    new ErrorResponse(
                        "El cliente con ID especificado no existe",
                        "CST001"
                    )
                )
                .build();
        }
        
        // 4️⃣ Mapeamos a response y devolvemos 200 OK
        UpdateCustomerResponse response =
            customerMapper.fromDTOToUpdateResponse(updatedOptional.get());
        
        return Response
                .ok(response)
                .build();
    }
    
    @GET
    @Path("/{customerId}")
    public Response getCustomerById(
        @PathParam("customerId") String customerId
    ) {
        // 1️⃣ Llamamos al servicio
        Optional<CustomerDTO> optionalDto = customerService.getCustomerById(customerId);
        
        // 2️⃣ Si no existe el cliente, devolvemos 404
        if (optionalDto.isEmpty()) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(
                    new ErrorResponse(
                        "El cliente con ID especificado no existe",
                        "CST002"
                    )
                )
                .build();
        }
        
        // 3️⃣ Convertimos el DTO a Response y devolvemos 200 OK
        GetCustomerByIdResponse response =
            customerMapper.fromDTOToGetByIdResponse(optionalDto.get());
        
        return Response.ok(response).build();
    }
}
