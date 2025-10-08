package com.springonly.backend.mapper;

import com.springonly.backend.model.response.PaymentResponse;
import org.mapstruct.*;
import com.springonly.backend.model.entity.PaymentEntity;
import com.springonly.backend.model.dto.PaymentDto;
import com.springonly.backend.model.request.PaymentRequest;

@Mapper(componentModel = "cdi")
public interface PaymentMapper {
    PaymentDto toDto(PaymentEntity e);
    PaymentEntity toEntity(PaymentDto d);
    PaymentResponse toResponse(PaymentDto d);
    PaymentDto requestToDto(PaymentRequest r);
}
