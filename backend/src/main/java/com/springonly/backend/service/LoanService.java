package com.springonly.backend.service;

import com.springonly.backend.entity.LoanEntity;
import com.springonly.backend.mapper.LoanCreationMapper;
import com.springonly.backend.mapper.LoanMapper;
import com.springonly.backend.model.dto.LoanCreationRequestDTO;
import com.springonly.backend.model.dto.LoanCreationResponseDTO;
import com.springonly.backend.model.dto.LoanDTO;
import com.springonly.backend.repository.LoanRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ApplicationScoped
public class LoanService {
    @Inject
    LoanRepository loanRepository;

    @Inject
    LoanMapper loanMapper;

    @Inject
    LoanCreationMapper loanCreationMapper;

    public Integer createLoan(
        LoanDTO loanDTO
    ) {
        LoanEntity loanEntity = loanCreationMapper.fromDTOToEntity(loanDTO);

        return loanRepository.createLoan(loanEntity);
    }

    public LoanDTO getLoan(
        Integer loanId
    ) {
        LoanEntity loanEntity = loanRepository.findByLoanId(loanId);

        return loanMapper.fromEntityToDTO(loanEntity);
    }

    public List<LoanDTO> listLoan(
        String customerId
    ) {
        List<LoanEntity> loanEntities = loanRepository.findByCustomerId(customerId);

        return loanMapper.fromEntitiesToDTOs(loanEntities);
    }
}
