package com.springonly.backend.model.request;

import com.springonly.backend.model.dto.LoanCreationRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanCreationRequest {
    private LoanCreationRequestDTO loan;
}