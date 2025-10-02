package com.springonly.backend.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments", schema = "credits")
@IdClass(PaymentId.class)
public class Payment {

    @Id
    private Integer loanId;

    @Id
    private Short paymentNumber;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal principalAmount;

    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal interestAmount;

    @Column(length = 16, nullable = false)
    private String paymentStateId; // PENDIENTE, PAGADO

    @Column(nullable = false)
    private OffsetDateTime writtenAt;
}
