package com.springonly.backend.mapper;

import org.mapstruct.*;
import com.springonly.backend.model.entity.LoanEntity;
import com.springonly.backend.model.dto.LoanDto;
import com.springonly.backend.model.request.LoanRequest;
import com.springonly.backend.model.response.LoanResponse;

@Mapper(componentModel = "cdi")
public interface LoanMapper {
    LoanDto toDto(LoanEntity e);
    LoanEntity toEntity(LoanDto d);
    LoanResponse toResponse(LoanDto d);
    LoanDto requestToDto(LoanRequest r);
}
