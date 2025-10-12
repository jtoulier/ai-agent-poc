package com.springonly.backend.service;

import com.springonly.backend.model.dto.CustomerDTO;
import com.springonly.backend.repository.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CustomerService {
    @Inject
    CustomerRepository customerRepository;

    public Optional<List<CustomerDTO>> listCustomersByRelationshipManagerById(
        String relationshipManagerId
    ) {
        return customerRepository.listCustomersByRelationshipManagerById(relationshipManagerId);
    }
}
