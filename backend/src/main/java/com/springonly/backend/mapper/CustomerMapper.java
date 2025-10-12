package com.springonly.backend.mapper;

import com.springonly.backend.model.dto.CustomerDTO;
import com.springonly.backend.model.entity.CustomerEntity;
import com.springonly.backend.model.request.CreateCustomerRequest;
import com.springonly.backend.model.request.UpdateCustomerRequest;
import com.springonly.backend.model.response.CreateCustomerResponse;
import com.springonly.backend.model.response.GetCustomerByIdResponse;
import com.springonly.backend.model.response.UpdateCustomerResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface CustomerMapper {
    // Request -> DTO
    CustomerDTO fromCreateRequestToDTO(CreateCustomerRequest createCustomerRequest);
    CustomerDTO fromUpdateRequestToDTO(UpdateCustomerRequest updateCustomerRequest);

    // DTO <-> Entity
    CustomerDTO fromEntityToDTO(CustomerEntity CustomerEntity);
    CustomerEntity fromDTOToEntity(CustomerDTO customerDTO);

    // Response <- DTO
    CreateCustomerResponse fromDTOToCreateResponse(CustomerDTO customerDTO);
    UpdateCustomerResponse fromDTOToUpdateResponse(CustomerDTO customerDTO);
    GetCustomerByIdResponse fromDTOToGetByIdResponse(CustomerDTO customerDTO);

}
