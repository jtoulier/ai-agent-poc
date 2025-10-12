package com.springonly.backend.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class UpdateLoanResponse {
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
