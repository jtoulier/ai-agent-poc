package com.springonly.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanCreationRequestDTO {
    private String currencyId;
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private Short numberOfMonthlyPayments;
}