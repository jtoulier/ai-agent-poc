package com.springonly.backend.model.response;

import com.springonly.backend.model.dto.LoanDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanRetrievalResponse {
    private LoanDTO loan;
}