package com.springonly.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PaymentCreationRequest {
    private Integer loanId;
    private Short paymentNumber;
    private LocalDate dueDate;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
}
