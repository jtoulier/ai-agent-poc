package com.springonly.backend.mapper;

import com.springonly.backend.model.dto.LoanDTO;
import com.springonly.backend.model.entity.LoanEntity;
import com.springonly.backend.model.request.LoanRequest;
import com.springonly.backend.model.response.LoanResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface LoanMapper {
    LoanDTO fromRequest(LoanRequest req);
    LoanResponse toResponse(LoanDTO dto);
    LoanEntity toEntity(LoanDTO dto);
    LoanDTO toDTO(LoanEntity entity);
}
