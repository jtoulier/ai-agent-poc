package com.springonly.backend.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {
    private Integer loanId;
    private String customerId;
    private String currencyId;
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private LocalDate loanDisbursementDate;
    private Short numberOfMonthlyPayments;
    private String loanStateId;
    private OffsetDateTime writtenAt;
}
