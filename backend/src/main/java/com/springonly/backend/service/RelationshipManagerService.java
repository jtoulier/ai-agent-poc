package com.springonly.backend.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.springonly.backend.repository.RelationshipManagerRepository;
import com.springonly.backend.mapper.RelationshipManagerMapper;
import com.springonly.backend.model.dto.RelationshipManagerDto;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class RelationshipManagerService {

    @Inject
    RelationshipManagerRepository relationshipManagerRepository;

    @Inject
    RelationshipManagerMapper relationshipManagerMapper;

    public Optional<RelationshipManagerDto> login(RelationshipManagerDto dto) {
        Optional<RelationshipManagerDto> found = relationshipManagerRepository.findByIdDto(dto.getRelationshipManagerId());

        if (found.isEmpty()) {
            return Optional.empty();
        }

        RelationshipManagerDto r = found.get();

        if (r.getPassword() == null || !r.getPassword().equals(dto.getPassword())) {
            return Optional.empty();
        }

        // No persistimos ni modificamos nada
        r.setPassword(null); // <- para no devolver la clave
        return Optional.of(r);
    }


    public Optional<RelationshipManagerDto> getById(String id) {
        return relationshipManagerRepository.findByIdDto(id);
    }

    public RelationshipManagerDto update(String id, RelationshipManagerDto dto) {
        dto.setRelationshipManagerId(id);
        if (dto.getWrittenAt() == null) dto.setWrittenAt(OffsetDateTime.now());
        return relationshipManagerRepository.updateDto(dto);
    }
}
