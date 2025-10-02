package com.springonly.backend.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Integer loanId;
    private Short paymentNumber;
    private LocalDate dueDate;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal totalPaymentAmount;
    private String paymentStateId;
    private OffsetDateTime writtenAt;
}
