package com.springonly.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "customers", schema = "credits")
@Data
public class CustomerEntity {
    @Id
    private String customerId;

    private String customerName;
    private String customerTypeId;
    private String riskCategoryId;
    private BigDecimal lineOfCreditAmount;

    @ManyToOne
    @JoinColumn(name = "relationshipManagerId", nullable = false)
    private RelationshipManagerEntity relationshipManager;

    private OffsetDateTime writtenAt;
}
