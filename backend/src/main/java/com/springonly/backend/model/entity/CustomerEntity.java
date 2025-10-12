package com.springonly.backend.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(schema = "credits", name = "customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity {
    @Id
    private String customerId;
    private String customerName;
    private String customerTypeId;
    private String riskCategoryId;
    private BigDecimal lineOfCreditAmount;
    private String relationshipManagerId;
    private OffsetDateTime writtenAt;
}
