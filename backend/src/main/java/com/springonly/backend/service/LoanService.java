package com.springonly.backend.service;

import com.springonly.backend.model.dto.LoanDTO;
import com.springonly.backend.repository.LoanRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LoanService {
    @Inject
    LoanRepository loanRepository;
    
    public List<LoanDTO> listLoansByCustomerId(
        String customerId
    ) {
        return loanRepository.listLoansByCustomerId(customerId);
    }
    
    public LoanDTO createLoan(
        LoanDTO loanDTO
    ) {
        loanDTO.setLoanStateId("EN EVALUACION");
        loanDTO.setWrittenAt(OffsetDateTime.now());
        
        return loanRepository.createLoan(loanDTO);
    }
    
    public Optional<LoanDTO> updateLoan(
        LoanDTO loanDTO
    ) {
        loanDTO.setWrittenAt(OffsetDateTime.now());
        
        return loanRepository.updateLoan(loanDTO);
    }
    
    public Optional<LoanDTO> getLoanById(
        Integer loanId
    ) {
        return loanRepository.getLoanById(loanId);
    }
}
