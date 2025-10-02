package com.springonly.backend.mapper;

import org.mapstruct.Mapper;
import com.springonly.backend.model.entity.Loan;
import com.springonly.backend.model.dto.LoanDTO;
import com.springonly.backend.model.request.LoanRequest;
import com.springonly.backend.model.response.LoanResponse;

@Mapper(componentModel = "cdi")
public interface LoanMapper {
    LoanDTO fromRequest(LoanRequest req);
    LoanResponse toResponse(LoanDTO dto);
    Loan toEntity(LoanDTO dto);
    LoanDTO toDTO(Loan entity);
}
