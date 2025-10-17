package com.springonly.backend.mapper;

import com.springonly.backend.model.dto.LoanDTO;
import com.springonly.backend.model.entity.LoanEntity;
import com.springonly.backend.model.request.CreateLoanRequest;
import com.springonly.backend.model.request.UpdateLoanRequest;
import com.springonly.backend.model.response.CreateLoanResponse;
import com.springonly.backend.model.response.GetLoanByIdResponse;
import com.springonly.backend.model.response.UpdateLoanResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface LoanMapper {
    // Request -> DTO
    LoanDTO fromCreateRequestToDTO(CreateLoanRequest createLoanRequest);
    LoanDTO fromUpdateRequestToDTO(UpdateLoanRequest updateLoanRequest);

    // DTO <-> Entity
    LoanDTO fromEntityToDTO(LoanEntity loanEntity);
    LoanEntity fromDTOToEntity(LoanDTO loanDTO);

    // Response <- DTO
    CreateLoanResponse fromDTOToCreateResponse(LoanDTO loanDTO);
    UpdateLoanResponse fromDTOToUpdateResponse(LoanDTO loanDTO);
    GetLoanByIdResponse fromDTOToGetByIdResponse(LoanDTO loanDTO);
}
