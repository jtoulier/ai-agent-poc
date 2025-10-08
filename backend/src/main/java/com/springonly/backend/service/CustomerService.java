package com.springonly.backend.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.springonly.backend.repository.CustomerRepository;
import com.springonly.backend.mapper.CustomerMapper;
import com.springonly.backend.model.dto.CustomerDto;

import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class CustomerService {

    @Inject
    CustomerRepository customerRepository;

    @Inject
    CustomerMapper customerMapper;

    public CustomerDto create(CustomerDto dto) {
        if (dto.getWrittenAt() == null) dto.setWrittenAt(OffsetDateTime.now());
        return customerRepository.persistDto(dto);
    }

    public CustomerDto update(String id, CustomerDto dto) {
        dto.setCustomerId(id);
        if (dto.getWrittenAt() == null) dto.setWrittenAt(OffsetDateTime.now());
        return customerRepository.updateDto(dto);
    }

    public CustomerDto getById(String id) {
        return customerRepository.findByIdDto(id);
    }

    public List<CustomerDto> listByRelationshipManagerId(String rmId) {
        return customerRepository.findByRelationshipManagerId(rmId);
    }
}
