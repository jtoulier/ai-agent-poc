package com.springonly.backend.repository;

import com.springonly.backend.mapper.CustomerMapper;
import com.springonly.backend.model.dto.CustomerDTO;
import com.springonly.backend.model.entity.CustomerEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

public class CustomerRepository implements PanacheRepositoryBase<CustomerEntity, String> {
    @Inject
    CustomerMapper customerMapper;

    public Optional<List<CustomerDTO>> listCustomersByRelationshipManagerById(
            String relationshipManagerId
    ) {
        // Obtenemos la lista de entidades filtrando por relationshipManagerId
        List<CustomerEntity> entities = list("relationshipManagerId", relationshipManagerId);

        // Si no hay resultados, retornamos Optional vacío
        if (entities.isEmpty()) {
            return Optional.empty();
        }

        // Transformamos cada CustomerEntity a CustomerDTO usando el mapper
        List<CustomerDTO> dtos = entities.stream()
                .map(customerMapper::fromEntityToDTO)
                .toList();

        return Optional.of(dtos);
    }
}