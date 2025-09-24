package com.springonly.backend.repository;

import com.springonly.backend.entity.RelationshipManagerEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RelationshipManagerRepository implements PanacheRepository<RelationshipManagerEntity> {
}
