package com.springonly.backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentIdEntity implements Serializable {
    private Integer loanId;
    private Short paymentNumber;
}
