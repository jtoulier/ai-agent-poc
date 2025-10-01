package com.springonly.backend.mapper;

import com.springonly.backend.entity.CustomerEntity;
import com.springonly.backend.model.dto.CustomerDTO;
import com.springonly.backend.model.response.CustomerRetrievalResponse;
import com.springonly.backend.model.response.CustomersRetrievalResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface CustomerMapper {
    CustomerDTO fromEntityToDTO(CustomerEntity customerEntity);
    List<CustomerDTO> fromEntitiesToDTOs(List<CustomerEntity> customerEntities);

    CustomerRetrievalResponse fromDTOToResponse(CustomerDTO customerDTO);
    CustomersRetrievalResponse fromDTOsToResponse(List<CustomerDTO> customerDTOs);
}
