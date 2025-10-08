package com.springonly.backend.mapper;

import org.mapstruct.*;
import com.springonly.backend.model.entity.RelationshipManagerEntity;
import com.springonly.backend.model.dto.RelationshipManagerDto;
import com.springonly.backend.model.request.RelationshipManagerUpdateRequest;
import com.springonly.backend.model.request.RelationshipManagerLoginRequest;
import com.springonly.backend.model.response.RelationshipManagerResponse;
import com.springonly.backend.model.response.RelationshipManagerLoginResponse;

@Mapper(componentModel = "cdi")
public interface RelationshipManagerMapper {
    RelationshipManagerDto toDto(RelationshipManagerEntity e);
    RelationshipManagerEntity toEntity(RelationshipManagerDto d);
    RelationshipManagerResponse toResponse(RelationshipManagerDto d);
    RelationshipManagerLoginResponse toLoginResponse(RelationshipManagerDto d);
    RelationshipManagerDto loginRequestToDto(RelationshipManagerLoginRequest r);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDtoFromRequest(RelationshipManagerUpdateRequest req, @MappingTarget RelationshipManagerDto dto);
}
