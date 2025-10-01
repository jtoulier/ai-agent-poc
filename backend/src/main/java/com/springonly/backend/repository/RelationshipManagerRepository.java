package com.springonly.backend.repository;

import com.springonly.backend.entity.RelationshipManagerEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RelationshipManagerRepository implements PanacheRepository<RelationshipManagerEntity> {
    public RelationshipManagerEntity login(
        String relationshipManagerId,
        String password
    ) {
        String sql = "relationshipManagerId = ?1 and password = ?2";

        RelationshipManagerEntity relationshipManagerEntity =
            find(
                sql,
                relationshipManagerId,
                password
            )
            .firstResult();

        return relationshipManagerEntity;
    }
}