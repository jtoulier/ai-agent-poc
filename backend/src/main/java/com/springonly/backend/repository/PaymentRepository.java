package com.springonly.backend.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import com.springonly.backend.model.entity.PaymentEntity;
import com.springonly.backend.model.entity.PaymentIdEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class PaymentRepository implements PanacheRepositoryBase<PaymentEntity, PaymentId> {

    @PersistenceContext
    EntityManager em;

    public List<PaymentEntity> findByLoanId(Integer loanId) {
        TypedQuery<PaymentEntity> q = em.createQuery(
                "SELECT p FROM PaymentEntity p WHERE p.loanId = :lid ORDER BY p.paymentNumber",
                PaymentEntity.class);
        q.setParameter("lid", loanId);
        return q.getResultList();
    }

    public PaymentEntity findById(Integer loanId, Short paymentNumber) {
        return em.find(PaymentEntity.class, new PaymentIdEntity(loanId, paymentNumber));
    }
}
