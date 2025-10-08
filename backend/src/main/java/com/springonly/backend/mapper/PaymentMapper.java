package com.springonly.backend.mapper;

import com.springonly.backend.model.dto.PaymentDTO;
import com.springonly.backend.model.entity.PaymentEntity;
import com.springonly.backend.model.request.PaymentRequest;
import com.springonly.backend.model.response.PaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface PaymentMapper {

    PaymentDTO fromRequest(PaymentRequest req);

    @Mapping(target = "totalPaymentAmount", expression = "java(dto.getPrincipalAmount().add(dto.getInterestAmount()))")
    PaymentResponse toResponse(PaymentDTO dto);

    PaymentEntity toEntity(PaymentDTO dto);

    PaymentDTO toDTO(PaymentEntity entity);
}
