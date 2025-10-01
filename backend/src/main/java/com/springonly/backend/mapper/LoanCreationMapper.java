package com.springonly.backend.mapper;

import com.springonly.backend.entity.LoanEntity;
import com.springonly.backend.model.dto.LoanDTO;
import com.springonly.backend.model.request.LoanCreationRequest;
import com.springonly.backend.model.response.LoanCreationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface LoanCreationMapper {
    LoanDTO fromRequestToDTO(LoanCreationRequest loanCreationRequest);
    LoanEntity fromDTOToEntity(LoanDTO loanDTO);
    LoanDTO fromEntityToDTO(LoanEntity loanEntity);
    LoanCreationResponse fromDTOToResponse(LoanDTO loanDTO);
}