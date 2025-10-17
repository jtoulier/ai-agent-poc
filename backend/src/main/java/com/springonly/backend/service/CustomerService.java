package com.springonly.backend.service;

import com.springonly.backend.model.dto.CustomerDTO;
import com.springonly.backend.repository.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CustomerService {
    @Inject
    CustomerRepository customerRepository;

    public List<CustomerDTO> listCustomersByRelationshipManagerById(
        String relationshipManagerId
    ) {
        return customerRepository.listCustomersByRelationshipManagerById(relationshipManagerId);
    }
    
    public CustomerDTO createCustomer(
        CustomerDTO customerDTO
    ) {
        customerDTO.setWrittenAt(OffsetDateTime.now());
        
        return customerRepository.createCustomer(customerDTO);
    }
    
    public Optional<CustomerDTO> updateCustomer(
        CustomerDTO customerDTO
    ) {
        customerDTO.setWrittenAt(OffsetDateTime.now());
        
        return customerRepository.updateCustomer(customerDTO);
    }
    
    public Optional<CustomerDTO> getCustomerById(
        String customerId
    ) {
        return customerRepository.getCustomerById(customerId);
    }
}
