package com.springonly.backend.repository;

import com.springonly.backend.entity.LoanEntity;
import com.springonly.backend.mapper.LoanMapper;
import com.springonly.backend.model.dto.LoanCreationRequestDTO;
import com.springonly.backend.model.dto.LoanDTO;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class LoanRepository implements PanacheRepositoryBase<LoanEntity, Integer> {
    @Inject
    LoanMapper loanMapper;

    public LoanEntity findByLoanId(Integer loanId) {
        return findById(
            loanId
        );
    }

    public List<LoanEntity> findByCustomerId(String customerId) {
        return find(
            "customer.customerId",
            customerId
        )
        .list();
    }

    public Integer createLoan(
        LoanEntity loanEntity
    ) {
        persistAndFlush(loanEntity);

        return loanEntity.getLoanId();
    }
}
