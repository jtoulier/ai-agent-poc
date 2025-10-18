package com.springonly.backend.mapper;

import com.springonly.backend.model.dto.PaymentDTO;
import com.springonly.backend.model.entity.PaymentEntity;
import com.springonly.backend.model.request.CreatePaymentRequest;
import com.springonly.backend.model.request.UpdatePaymentRequest;
import com.springonly.backend.model.response.CreatePaymentResponse;
import com.springonly.backend.model.response.GetPaymentByIdResponse;
import com.springonly.backend.model.response.UpdatePaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "cdi")
public interface PaymentMapper {
    // Request -> DTO
    PaymentDTO fromCreateRequestToDTO(CreatePaymentRequest createPaymentRequest);
    PaymentDTO fromUpdateRequestToDTO(UpdatePaymentRequest updatePaymentRequest);

    // DTO <-> Entity
    @Mappings({
        @Mapping(target = "loanId", source = "id.loanId"),
        @Mapping(target = "paymentNumber", source = "id.paymentNumber")
    })
    PaymentDTO fromEntityToDTO(PaymentEntity paymentEntity);
    
    @Mappings({
        @Mapping(target = "id.loanId", source = "loanId"),
        @Mapping(target = "id.paymentNumber", source = "paymentNumber")
    })
    PaymentEntity fromDTOToEntity(PaymentDTO paymentDTO);

    // Response <- DTO
    CreatePaymentResponse fromDTOToCreateResponse(PaymentDTO paymentDTO);
    UpdatePaymentResponse fromDTOToUpdateResponse(PaymentDTO paymentDTO);
    
    @Mappings({
        @Mapping(target = "loanId", source = "loanId"),
        @Mapping(target = "paymentNumber", source = "paymentNumber")
    })
    GetPaymentByIdResponse fromDTOToGetByIdResponse(PaymentDTO paymentDTO);
}
