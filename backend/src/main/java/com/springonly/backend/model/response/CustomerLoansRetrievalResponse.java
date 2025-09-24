package com.springonly.backend.model.response;

import com.springonly.backend.model.response.auxiliar.LoanResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLoansRetrievalResponse {
    private List<LoanResponse> loans;
}