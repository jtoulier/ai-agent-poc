package com.springonly.backend.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import com.springonly.backend.model.entity.RelationshipManagerEntity;
import com.springonly.backend.model.dto.RelationshipManagerDto;
import com.springonly.backend.mapper.RelationshipManagerMapper;

import java.util.Optional;

@ApplicationScoped
public class RelationshipManagerRepository implements PanacheRepositoryBase<RelationshipManagerEntity, String> {

    @Inject
    RelationshipManagerMapper relationshipManagerMapper;

    public Optional<RelationshipManagerDto> findByIdDto(String id) {
        RelationshipManagerEntity e = find("relationshipManagerId", id).firstResult();
        if (e == null) return Optional.empty();
        return Optional.of(relationshipManagerMapper.toDto(e));
    }

    public RelationshipManagerDto persistDto(RelationshipManagerDto dto) {
        RelationshipManagerEntity e = relationshipManagerMapper.toEntity(dto);
        persist(e);
        return relationshipManagerMapper.toDto(e);
    }

    public RelationshipManagerDto updateDto(RelationshipManagerDto dto) {
        RelationshipManagerEntity e = findById(dto.getRelationshipManagerId());
        if (e == null) return persistDto(dto);
        e.setThreadId(dto.getThreadId());
        e.setWrittenAt(dto.getWrittenAt());
        persist(e);
        return relationshipManagerMapper.toDto(e);
    }
}
