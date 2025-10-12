package com.springonly.backend.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

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
