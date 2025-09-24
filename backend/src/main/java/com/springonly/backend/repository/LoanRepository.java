package com.springonly.backend.repository;

import com.springonly.backend.entity.LoanEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LoanRepository implements PanacheRepository<LoanEntity> {
}
