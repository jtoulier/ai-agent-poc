package com.springonly.backend.model.response;

import com.springonly.backend.model.dto.CustomerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomersRetrievalResponse {
    private List<CustomerDTO> customers;
}