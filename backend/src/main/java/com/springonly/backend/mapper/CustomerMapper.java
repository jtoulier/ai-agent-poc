package com.springonly.backend.mapper;

import org.mapstruct.Mapper;
import com.springonly.backend.model.entity.Customer;
import com.springonly.backend.model.dto.CustomerDTO;
import com.springonly.backend.model.request.CustomerRequest;
import com.springonly.backend.model.response.CustomerResponse;

@Mapper(componentModel = "cdi")
public interface CustomerMapper {

    // Request <-> DTO
    CustomerDTO fromRequest(CustomerRequest req);

    // DTO <-> Response
    CustomerResponse toResponse(CustomerDTO dto);

    // Entity <-> DTO
    Customer toEntity(CustomerDTO dto);
    CustomerDTO toDTO(Customer entity);
}
