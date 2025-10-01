package com.springonly.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "loans", schema = "credits")
@Data
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer loanId;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private CustomerEntity customer;

    private String currencyId;
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private LocalDate loanDisbursementDate;
    private Short numberOfMonthlyPayments;
    private String loanStateId;
    private OffsetDateTime writtenAt;
}
