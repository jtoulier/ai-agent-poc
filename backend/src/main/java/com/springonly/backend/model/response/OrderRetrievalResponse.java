package com.springonly.backend.model.response;

import com.springonly.backend.model.response.auxiliar.CreditRetrievalAuxiliarResponse;
import com.springonly.backend.model.response.auxiliar.OrderRetrievalAuxiliarResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRetrievalResponse {
    private OrderRetrievalAuxiliarResponse order;
    private CreditRetrievalAuxiliarResponse credit;
}