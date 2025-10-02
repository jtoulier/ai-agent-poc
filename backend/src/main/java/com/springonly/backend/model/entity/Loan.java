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
@Table(name = "loans", schema = "credits")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer loanId;

    @Column(length = 16, nullable = false)
    private String customerId;

    @Column(length = 3, nullable = false)
    private String currencyId; // PEN, USD

    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal principalAmount;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private LocalDate loanDisbursementDate;

    @Column(nullable = false)
    private Short numberOfMonthlyPayments;

    @Column(length = 16, nullable = false)
    private String loanStateId; // EN EVALUACION, VIGENTE, DESAPROBADO, CANCELADO

    @Column(nullable = false)
    private OffsetDateTime writtenAt;
}
