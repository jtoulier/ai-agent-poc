package com.springonly.backend.mapper;

import com.springonly.backend.model.dto.CustomerDTO;
import com.springonly.backend.model.entity.CustomerEntity;
import com.springonly.backend.model.request.CustomerRequest;
import com.springonly.backend.model.response.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    CustomerDTO toDto(CustomerEntity entity);

    CustomerEntity toEntity(CustomerDTO dto);

    CustomerResponse toResponse(CustomerDTO dto);
}
