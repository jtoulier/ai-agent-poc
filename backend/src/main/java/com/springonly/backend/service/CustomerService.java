package com.springonly.backend.service;

import com.springonly.backend.model.dto.CustomerDTO;
import com.springonly.backend.repository.CustomerRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class CustomerService {

    @Inject
    CustomerRepository repo;

    public List<CustomerDTO> listAll() {
        return repo.listAllDTOs();
    }

    public CustomerDTO getById(String id) {
        return repo.findDTOById(id);
    }

    public CustomerDTO create(CustomerDTO dto) {
        dto.setWrittenAt(OffsetDateTime.now());
        return repo.save(dto);
    }

    public CustomerDTO patch(CustomerDTO dto) {
        dto.setWrittenAt(OffsetDateTime.now());
        return repo.update(dto);
    }
}
