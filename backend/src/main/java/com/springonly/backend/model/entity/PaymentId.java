package com.springonly.backend.model.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentId implements Serializable {
    private Integer loanId;
    private Short paymentNumber;
}
