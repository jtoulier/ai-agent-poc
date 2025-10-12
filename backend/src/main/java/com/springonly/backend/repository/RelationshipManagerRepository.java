package com.springonly.backend.repository;

import com.springonly.backend.mapper.RelationshipManagerMapper;
import com.springonly.backend.model.dto.RelationshipManagerDTO;
import com.springonly.backend.model.entity.RelationshipManagerEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class RelationshipManagerRepository implements PanacheRepositoryBase<RelationshipManagerEntity, String> {

    @Inject
    RelationshipManagerMapper relationshipManagerMapper;

    public Optional<RelationshipManagerDTO> loginRelationshipManager(
        RelationshipManagerDTO relationshipManagerDTO
    ) {
        return Optional
                .ofNullable(
                    findById(relationshipManagerDTO.getRelationshipManagerId())
                )
                .filter(
entity -> Objects.equals(entity.getPassword(), relationshipManagerDTO.getPassword())
                )
                .map(relationshipManagerMapper::fromEntityToDTO);
    }


    public Optional<RelationshipManagerDTO> updateRelationshipManagerThreadId(
        RelationshipManagerDTO relationshipManagerDTO
    ) {
        return Optional.ofNullable(findById(relationshipManagerDTO.getRelationshipManagerId()))
                .map(entity -> {
                    entity.setThreadId(relationshipManagerDTO.getThreadId());
                    entity.setWrittenAt(relationshipManagerDTO.getWrittenAt());
                    // Hibernate detecta los cambios y hace UPDATE al finalizar la transacción
                    return relationshipManagerMapper.fromEntityToDTO(entity);
                });
    }

    public Optional<RelationshipManagerDTO> getRelationshipManagerById(
        String relationshipManagerId
    ) {
        return Optional
                .ofNullable(
                    findById(relationshipManagerId)
                )
                .map(relationshipManagerMapper::fromEntityToDTO);
    }



}
