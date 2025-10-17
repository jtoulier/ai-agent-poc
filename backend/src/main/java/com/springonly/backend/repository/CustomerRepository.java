package com.springonly.backend.repository;

import com.springonly.backend.mapper.CustomerMapper;
import com.springonly.backend.model.dto.CustomerDTO;
import com.springonly.backend.model.entity.CustomerEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CustomerRepository implements PanacheRepositoryBase<CustomerEntity, String> {
    @Inject
    CustomerMapper customerMapper;

    public List<CustomerDTO> listCustomersByRelationshipManagerById(
        String relationshipManagerId
    ) {
        return list("relationshipManagerId", relationshipManagerId)
                .stream()
                .map(customerMapper::fromEntityToDTO)
                .toList();
    }

    public CustomerDTO createCustomer(
        CustomerDTO customerDTO
    ) {
        CustomerEntity customerEntity = customerMapper.fromDTOToEntity(customerDTO);
        persist(customerEntity);

        return customerMapper.fromEntityToDTO(customerEntity);
    }

    public Optional<CustomerDTO> updateCustomer(
        CustomerDTO customerDTO
    ) {
        return Optional.ofNullable(findById(customerDTO.getCustomerId()))
            .map(entity -> {
                entity.setCustomerName(customerDTO.getCustomerName());
                entity.setCustomerTypeId(customerDTO.getCustomerTypeId());
                entity.setRiskCategoryId(customerDTO.getRiskCategoryId());
                entity.setLineOfCreditAmount(customerDTO.getLineOfCreditAmount());
                entity.setRelationshipManagerId(customerDTO.getRelationshipManagerId());
                entity.setWrittenAt(customerDTO.getWrittenAt());

                return customerMapper.fromEntityToDTO(entity);
            });
    }

    public Optional<CustomerDTO> getCustomerById(
        String customerId
    ) {
        return Optional.
                ofNullable(findById(customerId))
                .map(customerMapper::fromEntityToDTO);
    }
}