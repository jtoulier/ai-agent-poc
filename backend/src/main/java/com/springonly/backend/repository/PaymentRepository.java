package com.springonly.backend.repository;

import com.springonly.backend.entity.PaymentEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PaymentRepository implements PanacheRepositoryBase<PaymentEntity, PaymentEntity.PaymentId> {
    public List<PaymentEntity> findByLoanId(
        Integer loanId
    ) {
        return find("loan.loanId", loanId).list();
    }

    public void createPayment(
        PaymentEntity paymentEntity
    ) {
        persist(paymentEntity);
    }
}
