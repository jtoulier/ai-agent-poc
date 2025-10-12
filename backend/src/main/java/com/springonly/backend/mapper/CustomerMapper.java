package com.springonly.backend.mapper;

import com.springonly.backend.model.dto.CustomerDTO;
import com.springonly.backend.model.entity.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface CustomerMapper {
    CustomerDTO fromEntityToDTO(CustomerEntity CustomerEntity);
    CustomerEntity fromDTOToEntity(CustomerDTO customerDTO);

}
