package com.springonly.backend.repository;

import com.springonly.backend.entity.OrderEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<OrderEntity> {
}
