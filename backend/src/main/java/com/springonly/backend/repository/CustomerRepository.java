package com.springonly.backend.repository;

import com.springonly.backend.entity.CustomerEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CustomerRepository implements PanacheRepositoryBase<CustomerEntity, String> {
    public CustomerEntity findByCustomerId(
        String customerId
    ) {
        return findById(
            customerId
        );
    }

    public List<CustomerEntity> findByRelationshipManagerId(
        String relationshipManagerId
    ) {
        return find(
    "relationshipManager.relationshipManagerId",
          relationshipManagerId
        )
        .list();
    }
}
