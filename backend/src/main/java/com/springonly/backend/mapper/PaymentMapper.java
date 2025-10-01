package com.springonly.backend.mapper;

import com.springonly.backend.entity.PaymentEntity;
import com.springonly.backend.model.dto.PaymentDTO;
import com.springonly.backend.model.response.PaymentPaymentResponse;
import com.springonly.backend.model.response.PaymentsRetrievalResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface PaymentMapper {
    PaymentDTO fromEntityToDTO(PaymentEntity paymentEntity);
    List<PaymentDTO> fromEntitiesToDTOs(List<PaymentEntity> paymentEntities);

    PaymentPaymentResponse fromDTOToResponse(PaymentDTO paymentDTO);
    PaymentsRetrievalResponse fromDTOsToResponse(List<PaymentDTO> paymentDTOs);
}
