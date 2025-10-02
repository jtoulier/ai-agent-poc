package com.springonly.backend.repository;

import com.springonly.backend.mapper.RelationshipManagerMapper;
import com.springonly.backend.model.dto.RelationshipManagerDTO;
import com.springonly.backend.model.entity.RelationshipManager;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RelationshipManagerRepository implements PanacheRepositoryBase<RelationshipManager, String> {

    @Inject
    RelationshipManagerMapper mapper;

    public List<RelationshipManagerDTO> listAllDTOs() {
        return listAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public RelationshipManagerDTO findDTOById(String id) {
        RelationshipManager entity = findById(id);
        return entity != null ? mapper.toDTO(entity) : null;
    }

    public RelationshipManagerDTO save(RelationshipManagerDTO dto) {
        RelationshipManager entity = mapper.toEntity(dto);
        // seguridad: si entity.writtenAt es null, lo asignamos
        if (entity.getWrittenAt() == null) {
            entity.setWrittenAt(OffsetDateTime.now());
        }
        persist(entity);
        return mapper.toDTO(entity);
    }

    public RelationshipManagerDTO update(RelationshipManagerDTO dto) {
        RelationshipManager entity = mapper.toEntity(dto);
        // setear writtenAt en merge para reflejar la actualización
        entity.setWrittenAt(OffsetDateTime.now());
        getEntityManager().merge(entity);
        return mapper.toDTO(entity);
    }

    public RelationshipManagerDTO login(String relationshipManagerId, String password) {
        RelationshipManager entity = find("relationshipManagerId = ?1 and password = ?2", relationshipManagerId, password)
                .firstResult();
        return entity != null ? mapper.toDTO(entity) : null;
    }

}
