package com.springonly.backend.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import com.springonly.backend.model.entity.LoanEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class LoanRepository implements PanacheRepositoryBase<LoanEntity, Integer> {

    @PersistenceContext
    EntityManager em;

    public List<LoanEntity> findByCustomerId(String customerId) {
        TypedQuery<LoanEntity> q = em.createQuery(
                "SELECT l FROM LoanEntity l WHERE l.customerId = :cid",
                LoanEntity.class);
        q.setParameter("cid", customerId);
        return q.getResultList();
    }

    public LoanEntity findById(Integer loanId) {
        return em.find(LoanEntity.class, loanId);
    }
}
