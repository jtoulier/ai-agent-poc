package com.springonly.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

    private String customerId;
    private String customerName;
    private String customerTypeId;
    private String riskCategoryId;
    private BigDecimal lineOfCreditAmount;
    private String relationshipManagerId;
}
