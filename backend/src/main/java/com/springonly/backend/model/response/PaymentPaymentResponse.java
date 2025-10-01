package com.springonly.backend.model.response;

import com.springonly.backend.model.dto.PaymentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentPaymentResponse {
    private PaymentDTO payment;
}