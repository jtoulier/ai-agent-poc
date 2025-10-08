package com.springonly.backend.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.springonly.backend.repository.LoanRepository;
import com.springonly.backend.mapper.LoanMapper;
import com.springonly.backend.model.dto.LoanDto;

import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class LoanService {

    @Inject
    LoanRepository loanRepository;

    @Inject
    LoanMapper loanMapper;

    public LoanDto create(LoanDto dto) {
        if (dto.getLoanStateId() == null) dto.setLoanStateId("EN EVALUACION");
        if (dto.getWrittenAt() == null) dto.setWrittenAt(OffsetDateTime.now());
        return loanRepository.persistDto(dto);
    }

    public LoanDto update(
        Integer id,
        LoanDto dto
    ) {
        dto.setLoanId(id);
        if (dto.getWrittenAt() == null) dto.setWrittenAt(OffsetDateTime.now());
        return loanRepository.updateDto(dto);
    }

    public LoanDto getById(Integer id) {
        return loanRepository.findByIdDto(id);
    }

    public List<LoanDto> listByCustomerId(String customerId) {
        return loanRepository.findByCustomerId(customerId);
    }
}
