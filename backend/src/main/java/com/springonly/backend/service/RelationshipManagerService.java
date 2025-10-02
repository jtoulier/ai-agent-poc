package com.springonly.backend.service;

import com.springonly.backend.model.dto.RelationshipManagerDTO;
import com.springonly.backend.repository.RelationshipManagerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class RelationshipManagerService {

    @Inject
    RelationshipManagerRepository repo;

    public List<RelationshipManagerDTO> listAll() {
        return repo.listAllDTOs();
    }

    public RelationshipManagerDTO getById(String id) {
        return repo.findDTOById(id);
    }

    public RelationshipManagerDTO create(RelationshipManagerDTO dto) {
        dto.setWrittenAt(OffsetDateTime.now());
        return repo.save(dto);
    }

    public RelationshipManagerDTO patch(RelationshipManagerDTO dto) {
        dto.setWrittenAt(OffsetDateTime.now());
        return repo.update(dto);
    }

    public RelationshipManagerDTO login(String relationshipManagerId, String password) {
        return repo.login(relationshipManagerId, password);
    }

}
