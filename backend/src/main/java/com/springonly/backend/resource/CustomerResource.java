package com.springonly.backend.resource;

import com.springonly.backend.mapper.CustomerMapper;
import com.springonly.backend.mapper.LoanMapper;
import com.springonly.backend.model.dto.CustomerDTO;
import com.springonly.backend.model.dto.LoanDTO;
import com.springonly.backend.model.request.CreateCustomerRequest;
import com.springonly.backend.model.request.UpdateCustomerRequest;
import com.springonly.backend.model.response.CreateCustomerResponse;
import com.springonly.backend.model.response.GetCustomerByIdResponse;
import com.springonly.backend.model.response.GetLoanByIdResponse;
import com.springonly.backend.model.response.UpdateCustomerResponse;
import com.springonly.backend.model.response.generic.ErrorResponse;
import com.springonly.backend.service.CustomerService;
import com.springonly.backend.service.LoanService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    @Inject
    CustomerService customerService;
    
    @Inject
    CustomerMapper customerMapper;
    
    @Inject
    LoanService loanService;
    
    @Inject
    LoanMapper loanMapper;
    
    @HeaderParam("X-RelationshipManager-Id")
    String headerRelationshipManagerId;

    @POST
    @Path("/")
    @Transactional
    @Operation(operationId = "createCustomer", summary = "Create customer", description = "Create a new customer and return its created representation.")
    @APIResponses({
        @APIResponse(
            responseCode = "201",
            description = "Customer created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreateCustomerResponse.class)
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
    @Operation(operationId = "updateCustomer", summary = "Update customer", description = "Update customer data identified by customerId.")
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Customer updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UpdateCustomerResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "400",
            description = "Invalid request",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Customer not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
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
    @Operation(operationId = "getCustomerById", summary = "Get customer by id", description = "Retrieve customer details by customerId.")
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Customer retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GetCustomerByIdResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Customer not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
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
    
    @GET
    @Path("/{customerId}/loans")
    @Operation(operationId = "listLoansByCustomerId", summary = "List loans by customer", description = "List loans associated with the specified customerId.")
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Loans list for customer",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = SchemaType.ARRAY, implementation = GetLoanByIdResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "No loans found for customer",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public Response listLoansByCustomerId(
        @PathParam("customerId") String customerId
    ) {
        // 1️⃣ Obtenemos la lista de préstamos del servicio
        List<LoanDTO> dtos = loanService.listLoansByCustomerId(customerId);
        
        // 2️⃣ Si la lista está vacía, devolvemos un 404 o lista vacía según la política de negocio
        if (dtos.isEmpty()) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(
                    new ErrorResponse(
                        "El cliente indicado no tiene préstamos registrados",
                        "LN001"
                    )
                )
                .build();
        }
        
        // 3️⃣ Convertimos los DTO a Response
        List<GetLoanByIdResponse> responses =
            dtos
                .stream()
                .map(loanMapper::fromDTOToGetByIdResponse)
                .sorted(Comparator.comparing(GetLoanByIdResponse::getLoanId))
                .toList();
        
        // 4️⃣ Retornamos la lista de préstamos con 200 OK
        return Response
                .ok(responses)
                .build();
    }
}