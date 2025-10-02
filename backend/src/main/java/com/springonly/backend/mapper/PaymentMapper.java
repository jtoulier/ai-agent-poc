package com.springonly.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.springonly.backend.model.entity.Payment;
import com.springonly.backend.model.dto.PaymentDTO;
import com.springonly.backend.model.request.PaymentRequest;
import com.springonly.backend.model.response.PaymentResponse;

@Mapper(componentModel = "cdi")
public interface PaymentMapper {

    PaymentDTO fromRequest(PaymentRequest req);

    @Mapping(target = "totalPaymentAmount", expression = "java(dto.getPrincipalAmount().add(dto.getInterestAmount()))")
    PaymentResponse toResponse(PaymentDTO dto);

    Payment toEntity(PaymentDTO dto);

    PaymentDTO toDTO(Payment entity);
}
