package com.springonly.backend.mapper;

import com.springonly.backend.entity.PaymentEntity;
import com.springonly.backend.model.dto.PaymentCreationDTO;
import com.springonly.backend.model.request.PaymentCreationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface PaymentCreationMapper {
    PaymentCreationDTO fromRequestToDTO(PaymentCreationRequest paymentCreationRequest);
    PaymentEntity fromDTOToEntity(PaymentCreationDTO paymentCreationDTO);
}
