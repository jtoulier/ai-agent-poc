package com.springonly.backend.mapper;

import com.springonly.backend.entity.RelationshipManagerEntity;
import com.springonly.backend.model.dto.RelationshipManagerDTO;
import com.springonly.backend.model.request.LoginRequest;
import com.springonly.backend.model.response.LoginResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface LoginMapper {
    RelationshipManagerDTO fromRequestToDTO(LoginRequest loginRequest);
    RelationshipManagerEntity fromDTOToEntity(RelationshipManagerDTO relationshipManagerDTO);
    RelationshipManagerDTO fromEntityToDTO(RelationshipManagerEntity relationshipManagerEntity);
    LoginResponse fromDTOToResponse(RelationshipManagerDTO relationshipManagerDTO);
}