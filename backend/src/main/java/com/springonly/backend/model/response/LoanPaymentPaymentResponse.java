package com.springonly.backend.model.response;

import com.springonly.backend.model.response.auxiliar.PaymentResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanPaymentPaymentResponse {
    private Integer loanId;
    private Short paymentNumber;
    private LocalDate dueDate;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal totalPaymentAmount;
    private String paymentStateId;
    private LocalDate paymentDate;
    private OffsetDateTime writtenAt;
}