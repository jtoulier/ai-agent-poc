package com.springonly.backend.model.response;

import com.springonly.backend.model.dto.LoanCreationResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoanCreationResponse {
    private LoanCreationResponseDTO loan;
}
