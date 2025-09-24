package com.springonly.backend.repository;

import com.springonly.backend.entity.CustomerEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<CustomerEntity> {
}
