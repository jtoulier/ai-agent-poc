package com.springonly.backend.mapper;

import org.mapstruct.*;
import com.springonly.backend.model.entity.RelationshipManagerEntity;
import com.springonly.backend.model.dto.RelationshipManagerDTO;
import com.springonly.backend.model.request.LoginRequest;
import com.springonly.backend.model.request.UpdateRelationshipManagerRequest;
import com.springonly.backend.model.response.RelationshipManagerLoginResponse;
import com.springonly.backend.model.response.RelationshipManagerResponse;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RelationshipManagerMapper {

    RelationshipManagerDTO toDto(RelationshipManagerEntity entity);

    RelationshipManagerEntity toEntity(RelationshipManagerDTO dto);

    RelationshipManagerDTO fromLoginRequestToDto(LoginRequest login);

    RelationshipManagerLoginResponse toLoginResponse(RelationshipManagerDTO dto);

    RelationshipManagerDTO fromUpdateRequestToDto(UpdateRelationshipManagerRequest req);

    RelationshipManagerResponse toResponse(RelationshipManagerDTO dto);
}
