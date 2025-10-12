package com.springonly.backend.service;

import com.springonly.backend.model.dto.RelationshipManagerDTO;
import com.springonly.backend.repository.RelationshipManagerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class RelationshipManagerService {
    @Inject
    RelationshipManagerRepository relationshipManagerRepository;

    public Optional<RelationshipManagerDTO> loginRelationshipManager(
        RelationshipManagerDTO relationshipManagerDTO
    ) {
        return relationshipManagerRepository.loginRelationshipManager(relationshipManagerDTO);
    }

    public Optional<RelationshipManagerDTO> updateRelationshipManagerThreadId(
        RelationshipManagerDTO relationshipManagerDTO
    ) {
        relationshipManagerDTO.setWrittenAt(OffsetDateTime.now());

        return relationshipManagerRepository.updateRelationshipManagerThreadId(relationshipManagerDTO);
    }

    public Optional<RelationshipManagerDTO> getRelationshipManagerById(
        String relationshipManagerId
    ) {
        return relationshipManagerRepository.getRelationshipManagerById(relationshipManagerId);
    }
}
