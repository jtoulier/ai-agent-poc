package com.springonly.backend.repository;

import com.springonly.backend.model.entity.Customer;
import com.springonly.backend.model.dto.CustomerDTO;
import com.springonly.backend.mapper.CustomerMapper;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CustomerRepository implements PanacheRepositoryBase<Customer, String> {

    @Inject
    CustomerMapper mapper;

    public List<CustomerDTO> listAllDTOs() {
        return listAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO findDTOById(String id) {
        Customer entity = findById(id);
        return entity != null ? mapper.toDTO(entity) : null;
    }

    public CustomerDTO save(CustomerDTO dto) {
        Customer entity = mapper.toEntity(dto);
        if (entity.getWrittenAt() == null) {
            entity.setWrittenAt(OffsetDateTime.now());
        }
        persist(entity);
        return mapper.toDTO(entity);
    }

    public CustomerDTO update(CustomerDTO dto) {
        Customer entity = mapper.toEntity(dto);
        entity.setWrittenAt(OffsetDateTime.now());
        getEntityManager().merge(entity);
        return mapper.toDTO(entity);
    }
}
