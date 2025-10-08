package com.springonly.backend.model.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentId implements Serializable {
    private Integer loanId;
    private Short paymentNumber;
}
