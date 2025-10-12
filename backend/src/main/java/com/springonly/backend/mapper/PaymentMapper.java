package com.springonly.backend.mapper;

import com.springonly.backend.model.dto.PaymentDTO;
import com.springonly.backend.model.entity.PaymentEntity;
import com.springonly.backend.model.request.CreatePaymentRequest;
import com.springonly.backend.model.request.UpdatePaymentRequest;
import com.springonly.backend.model.response.CreatePaymentResponse;
import com.springonly.backend.model.response.GetPaymentByIdResponse;
import com.springonly.backend.model.response.UpdatePaymentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface PaymentMapper {
    // Request -> DTO
    PaymentDTO fromCreateRequestToDTO(CreatePaymentRequest createPaymentRequest);
    PaymentDTO fromUpdateRequestToDTO(UpdatePaymentRequest updatePaymentRequest);

    // DTO <-> Entity
    PaymentDTO fromEntityToDTO(PaymentEntity paymentEntity);
    PaymentEntity fromDTOToEntity(PaymentDTO paymentDTO);

    // Response <- DTO
    CreatePaymentResponse fromDTOToCreateResponse(PaymentDTO paymentDTO);
    UpdatePaymentResponse fromDTOToUpdateResponse(PaymentDTO paymentDTO);
    GetPaymentByIdResponse fromDTOToGetByIdResponse(PaymentDTO paymentDTO);
}
