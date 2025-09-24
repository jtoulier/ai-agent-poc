package com.springonly.backend.model.response;

import com.springonly.backend.model.response.auxiliar.CustomerResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipManagerCustomersRetrievalResponse {
    private List<CustomerResponse> customers;
}