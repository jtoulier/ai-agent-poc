package com.springonly.backend.model.response;

import com.springonly.backend.model.dto.LoanDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoansRetrievalResponse {
    private List<LoanDTO> loans;
}