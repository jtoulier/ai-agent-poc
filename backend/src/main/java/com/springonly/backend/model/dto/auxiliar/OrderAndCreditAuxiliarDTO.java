package com.springonly.backend.model.dto.auxiliar;

import com.springonly.backend.model.dto.CreditDTO;
import com.springonly.backend.model.dto.OrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderAndCreditAuxiliarDTO {
    private OrderDTO order;
    private CreditDTO credit;
}