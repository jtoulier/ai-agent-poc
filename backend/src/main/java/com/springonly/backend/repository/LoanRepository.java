package com.springonly.backend.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import com.springonly.backend.model.entity.LoanEntity;
import com.springonly.backend.model.dto.LoanDto;
import com.springonly.backend.mapper.LoanMapper;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LoanRepository implements PanacheRepositoryBase<LoanEntity, Integer> {

    @Inject
    LoanMapper loanMapper;

    public LoanDto persistDto(LoanDto dto) {
        LoanEntity e = loanMapper.toEntity(dto);
        persist(e);
        return loanMapper.toDto(e);
    }

    public LoanDto updateDto(
        LoanDto dto
    ) {
        LoanEntity e = findById(dto.getLoanId());
        if (e == null) return persistDto(dto);
        e.setPrincipalAmount(dto.getPrincipalAmount());
        e.setInterestRate(dto.getInterestRate());
        e.setLoanDisbursementDate(dto.getLoanDisbursementDate());
        e.setNumberOfMonthlyPayments(dto.getNumberOfMonthlyPayments());
        e.setLoanStateId(dto.getLoanStateId());
        e.setWrittenAt(dto.getWrittenAt());
        persist(e);
        return loanMapper.toDto(e);
    }

    public List<LoanDto> findByCustomerId(String customerId) {
        return find("customerId", customerId)
                .stream()
                .map(loanMapper::toDto)
                .collect(Collectors.toList());
    }

    public LoanDto findByIdDto(Integer id) {
        LoanEntity e = findById(id);
        if (e == null) return null;
        return loanMapper.toDto(e);
    }
}
