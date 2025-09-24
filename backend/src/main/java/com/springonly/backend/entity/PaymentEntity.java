package com.springonly.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "payments", schema = "credits")
@Data
@IdClass(PaymentEntity.PaymentId.class)
public class PaymentEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "loanId", nullable = false)
    private LoanEntity loan;

    @Id
    private Short paymentNumber;

    private LocalDate dueDate;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;

    // Campo calculado en DB: totalPaymentAmount (no se mapea porque es computed column)
    private String paymentStateId;
    private OffsetDateTime writtenAt;

    @Data
    public static class PaymentId implements Serializable {
        private Integer loan;        // corresponde a loanId (LoanEntity PK)
        private Short paymentNumber;
    }
}
