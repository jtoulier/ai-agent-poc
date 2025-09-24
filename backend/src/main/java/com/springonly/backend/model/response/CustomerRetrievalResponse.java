package com.springonly.backend.model.response;

import com.springonly.backend.model.response.auxiliar.CustomerResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRetrievalResponse {
    private CustomerResponse customer;
}