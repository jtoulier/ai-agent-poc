package com.springonly.backend.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import com.springonly.backend.model.entity.CustomerEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class CustomerRepository implements PanacheRepositoryBase<CustomerEntity, String> {

    @PersistenceContext
    EntityManager em;

    public List<CustomerEntity> findByRelationshipManagerId(String relationshipManagerId) {
        TypedQuery<CustomerEntity> q = em.createQuery(
                "SELECT c FROM CustomerEntity c WHERE c.relationshipManagerId = :rm",
                CustomerEntity.class);
        q.setParameter("rm", relationshipManagerId);
        return q.getResultList();
    }

    public CustomerEntity findById(String customerId) {
        return em.find(CustomerEntity.class, customerId);
    }
}
