package com.springonly.backend.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers", schema = "credits")
public class Customer {

    @Id
    @Column(length = 16)
    private String customerId;

    @Column(length = 128, nullable = false)
    private String customerName;

    @Column(length = 16, nullable = false)
    private String customerTypeId; // MICRO, PEQUEÑA, MEDIANA, GRAN EMPRESA

    @Column(length = 2, nullable = false)
    private String riskCategoryId; // NO, PP, DE, DU, PE

    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal lineOfCreditAmount;

    @Column(length = 16, nullable = false)
    private String relationshipManagerId;

    @Column(nullable = false)
    private OffsetDateTime writtenAt;
}
