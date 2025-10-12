package com.springonly.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerResponse {
    private String customerId;
    private String customerName;
    private String customerTypeId;
    private String riskCategoryId;
    private BigDecimal lineOfCreditAmount;
    private String relationshipManagerId;
    private OffsetDateTime writtenAt;
}
