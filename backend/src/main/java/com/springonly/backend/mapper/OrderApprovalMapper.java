package com.springonly.backend.mapper;

import com.springonly.backend.model.dto.OrderDTO;
import com.springonly.backend.model.request.OrderApprovalRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface OrderApprovalMapper {
    OrderDTO fromOrderApprovalRequestToOrderDTO(OrderApprovalRequest orderApprovalRequest);
}
