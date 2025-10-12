package com.springonly.backend.mapper;

import com.springonly.backend.model.dto.RelationshipManagerDTO;
import com.springonly.backend.model.entity.RelationshipManagerEntity;
import com.springonly.backend.model.request.LoginRelationshipManagerRequest;
import com.springonly.backend.model.request.UpdateRelationshipManagerThreadIdRequest;
import com.springonly.backend.model.response.GetRelationshipManagerByIdResponse;
import com.springonly.backend.model.response.LoginRelationshipManagerResponse;
import com.springonly.backend.model.response.UpdateRelationshipManagerThreadIdResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface RelationshipManagerMapper {
    // Request -> DTO
    RelationshipManagerDTO fromLoginRequestToDTO(LoginRelationshipManagerRequest loginRelationshipManagerRequest);
    RelationshipManagerDTO fromUpdateThreadIdRequestToDTO(UpdateRelationshipManagerThreadIdRequest updateRelationshipManagerThreadIdRequest);

    // DTO <-> Entity
    RelationshipManagerDTO fromEntityToDTO(RelationshipManagerEntity relationshipManagerEntity);
    RelationshipManagerEntity fromDTOToEntity(RelationshipManagerDTO relationshipManagerDTO);

    // Response <- DTO
    LoginRelationshipManagerResponse fromDTOToLoginResponse(RelationshipManagerDTO relationshipManagerDTO);
    UpdateRelationshipManagerThreadIdResponse fromDTOToUpdateThreadIdResponse(RelationshipManagerDTO relationshipManagerDTOto);
    GetRelationshipManagerByIdResponse fromDTOToGetByIdResponse(RelationshipManagerDTO relationshipManagerDTO);
}