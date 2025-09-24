package com.springonly.backend.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface LoanPaymentPaymentMapper {
    // <- From Entity to DTO
    XxxDTO fromXxxEntityToXxxDTO(XxxEntity xxxEntity);

    // <- From DTO to Response
    XxxResponse fromXxxDTOToXxxResponse(XxxDTO xxxDTO);
}
