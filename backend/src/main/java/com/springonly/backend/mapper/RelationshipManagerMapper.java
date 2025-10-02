package com.springonly.backend.mapper;

import com.springonly.backend.model.dto.RelationshipManagerDTO;
import com.springonly.backend.model.entity.RelationshipManager;
import com.springonly.backend.model.request.RelationshipManagerRequest;
import com.springonly.backend.model.response.RelationshipManagerResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface RelationshipManagerMapper {

    // Request <-> DTO
    RelationshipManagerDTO fromRequest(RelationshipManagerRequest req);

    // DTO <-> Response
    RelationshipManagerResponse toResponse(RelationshipManagerDTO dto);

    // DTO <-> Entity
    RelationshipManager toEntity(RelationshipManagerDTO dto);
    RelationshipManagerDTO toDTO(RelationshipManager entity);
}
