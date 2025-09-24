package com.springonly.backend.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface CustomerLoanCreationMapper {
    // -> From Request to DTO
    XxxDTO fromXxxRequestToXxxDTO(XxxRequest xxxRequest);

    // -> From DTO to Entity
    XxxEntity fromXxxDTOToXxxEntity(XxxDTO xxxDTO);

    // <- From Entity to DTO
    XxxDTO fromXxxEntityToXxxDTO(XxxEntity xxxEntity);

    // <- From DTO to Response
    XxxResponse fromXxxDTOToXxxResponse(XxxDTO xxxDTO);
}
