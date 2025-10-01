package com.springonly.backend.mapper;

import com.springonly.backend.entity.LoanEntity;
import com.springonly.backend.model.dto.LoanDTO;
import com.springonly.backend.model.response.LoanRetrievalResponse;
import com.springonly.backend.model.response.LoansRetrievalResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface LoanMapper {
    LoanDTO fromEntityToDTO(LoanEntity loanEntity);
    List<LoanDTO> fromEntitiesToDTOs(List<LoanEntity> loanEntities);

    LoanRetrievalResponse fromDTOToResponse(LoanDTO loanDTO);
    LoansRetrievalResponse fromDTOsToResponse(List<LoanDTO> loanDTOS);
}