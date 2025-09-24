package com.springonly.backend.model.response;

import com.springonly.backend.model.response.auxiliar.LoanResponse;
import com.springonly.backend.model.response.auxiliar.PaymentResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLoanRetrievalResponse {
    private LoanResponse loan;
    private List<PaymentResponse> payments;
}