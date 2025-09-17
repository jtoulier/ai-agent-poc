package com.springonly.backend.mapper;

import com.springonly.backend.model.dto.OrderDTO;
import com.springonly.backend.model.request.OrderRejectionRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface OrderRejectionMapper {
    // From Request to DTO
    OrderDTO fromOrderRejectionRequestToOrderDTO(OrderRejectionRequest orderRejectionRequest);
}