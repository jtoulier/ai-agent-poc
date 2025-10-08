package com.springonly.backend.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.OffsetDateTime;
import java.util.Optional;
import com.springonly.backend.model.entity.RelationshipManagerEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RelationshipManagerRepository implements PanacheRepositoryBase<RelationshipManagerEntity, String> {

    @PersistenceContext
    EntityManager em;

    public Optional<RelationshipManagerEntity> findByIdOptional(String id) {
        return Optional.ofNullable(em.find(RelationshipManagerEntity.class, id));
    }

    public Optional<RelationshipManagerEntity> findByIdAndPassword(String id, String password) {
        TypedQuery<RelationshipManagerEntity> q = em.createQuery(
                "SELECT r FROM RelationshipManagerEntity r WHERE r.relationshipManagerId = :id AND r.password = :pwd",
                RelationshipManagerEntity.class);
        q.setParameter("id", id);
        q.setParameter("pwd", password);
        return q.getResultStream().findFirst();
    }

    @Transactional
    public RelationshipManagerEntity persistOrUpdate(RelationshipManagerEntity entity) {
        if (entity.getWrittenAt() == null) {
            entity.setWrittenAt(OffsetDateTime.now());
        } else {
            entity.setWrittenAt(OffsetDateTime.now()); // update timestamp
        }
        if (em.find(RelationshipManagerEntity.class, entity.getRelationshipManagerId()) == null) {
            em.persist(entity);
            return entity;
        } else {
            return em.merge(entity);
        }
    }
}
