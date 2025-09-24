package com.springonly.backend.repository;

import com.springonly.backend.entity.PaymentEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PaymentRepository implements PanacheRepository<PaymentEntity> {
}
