package com.springonly.backend.mapper;

import com.springonly.backend.entity.CreditEntity;
import com.springonly.backend.entity.OrderEntity;
import com.springonly.backend.model.dto.CreditDTO;
import com.springonly.backend.model.dto.OrderDTO;
import com.springonly.backend.model.response.OrderRetrievalResponse;
import com.springonly.backend.model.response.auxiliar.CreditRetrievalAuxiliarResponse;
import com.springonly.backend.model.response.auxiliar.OrderRetrievalAuxiliarResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi") // Para integración con Quarkus
public interface OrderRetrievalMapper {
    // From DTO to Response : Auxiliar
    CreditRetrievalAuxiliarResponse fromCreditDTOToCreditRetrievalAuxiliarResponse(CreditDTO creditDTO);
    OrderRetrievalAuxiliarResponse fromOrderDTOToOrderRetrievalAuxiliarResponse(OrderDTO orderDTO);

    // From DTO to Response : Main
    default OrderRetrievalResponse toOrderRetrievalResponse(OrderDTO orderDTO, CreditDTO creditDTO) {
        OrderRetrievalResponse response = new OrderRetrievalResponse();
        response.setOrder(fromOrderDTOToOrderRetrievalAuxiliarResponse(orderDTO));
        response.setCredit(fromCreditDTOToCreditRetrievalAuxiliarResponse(creditDTO));
        return response;
    }

    // From Entity to DTO
    OrderDTO fromOrderEntityToOrderDTO(OrderEntity orderEntity);
    CreditDTO fromCreditEntityToCreditDTO(CreditEntity creditEntity);

}