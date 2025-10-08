package com.springonly.backend.mapper;

import org.mapstruct.*;
import com.springonly.backend.model.entity.CustomerEntity;
import com.springonly.backend.model.dto.CustomerDto;
import com.springonly.backend.model.request.CustomerRequest;
import com.springonly.backend.model.response.CustomerResponse;

@Mapper(componentModel = "cdi")
public interface CustomerMapper {
    CustomerDto toDto(CustomerEntity e);
    CustomerEntity toEntity(CustomerDto d);
    CustomerResponse toResponse(CustomerDto d);
    CustomerDto requestToDto(CustomerRequest r);
}
