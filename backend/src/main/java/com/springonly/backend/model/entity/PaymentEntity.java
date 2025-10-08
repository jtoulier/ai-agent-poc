package com.springonly.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.LocalDate;

@Entity
@Table(schema = "credits", name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity {
    @EmbeddedId
    private PaymentId id;
    private LocalDate dueDate;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;

    @Column(insertable = false, updatable = false)
    private BigDecimal totalPaymentAmount;

    private String paymentStateId;
    private OffsetDateTime writtenAt;
}
