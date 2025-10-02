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
public class PaymentDTO {
    private Integer loanId;
    private Short paymentNumber;
    private LocalDate dueDate;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private String paymentStateId;
    private OffsetDateTime writtenAt;
}
