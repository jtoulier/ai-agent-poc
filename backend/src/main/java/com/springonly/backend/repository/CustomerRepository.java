package com.springonly.backend.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import com.springonly.backend.model.entity.CustomerEntity;
import com.springonly.backend.model.dto.CustomerDto;
import com.springonly.backend.mapper.CustomerMapper;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CustomerRepository implements PanacheRepositoryBase<CustomerEntity, String> {

    @Inject
    CustomerMapper customerMapper;

    public CustomerDto persistDto(CustomerDto dto) {
        CustomerEntity e = customerMapper.toEntity(dto);
        persist(e);
        return customerMapper.toDto(e);
    }

    public CustomerDto updateDto(CustomerDto dto) {
        CustomerEntity e = findById(dto.getCustomerId());
        if (e == null) return persistDto(dto);
        e.setCustomerName(dto.getCustomerName());
        e.setCustomerTypeId(dto.getCustomerTypeId());
        e.setRiskCategoryId(dto.getRiskCategoryId());
        e.setLineOfCreditAmount(dto.getLineOfCreditAmount());
        e.setRelationshipManagerId(dto.getRelationshipManagerId());
        e.setWrittenAt(dto.getWrittenAt());
        persist(e);
        return customerMapper.toDto(e);
    }

    public List<CustomerDto> findByRelationshipManagerId(String rmId) {
        return find("relationshipManagerId", rmId)
                .stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }

    public CustomerDto findByIdDto(String id) {
        CustomerEntity e = findById(id);
        if (e == null) return null;
        return customerMapper.toDto(e);
    }
}
