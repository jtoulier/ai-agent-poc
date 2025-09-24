package com.springonly.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLoanCreationRequest {
    private String currencyId;
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private Short numberOfMonthlyPayments;
}