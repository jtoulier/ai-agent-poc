package com.springonly.backend.service;

import com.springonly.backend.entity.CustomerEntity;
import com.springonly.backend.mapper.CustomerMapper;
import com.springonly.backend.model.dto.CustomerDTO;
import com.springonly.backend.repository.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ApplicationScoped
public class CustomerService {
    @Inject
    CustomerRepository customerRepository;

    @Inject
    CustomerMapper customerMapper;

    public CustomerDTO getCustomer(
        String customerId
    ) {
        CustomerEntity customerEntity = customerRepository.findByCustomerId(customerId);

        return customerMapper.fromEntityToDTO(customerEntity);
    }

    public List<CustomerDTO> listCustomers(
        String relationshipManagerId
    ) {
        List<CustomerEntity> customerEntities =
            customerRepository.findByRelationshipManagerId(
                relationshipManagerId
            );

        return customerMapper.fromEntitiesToDTOs(customerEntities);
    }
}
