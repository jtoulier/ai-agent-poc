package com.springonly.backend.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.springonly.backend.repository.PaymentRepository;
import com.springonly.backend.mapper.PaymentMapper;
import com.springonly.backend.model.dto.PaymentDto;

import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class PaymentService {

    @Inject
    PaymentRepository paymentRepository;

    @Inject
    PaymentMapper paymentMapper;

    public PaymentDto create(PaymentDto dto) {
        if (dto.getWrittenAt() == null) dto.setWrittenAt(OffsetDateTime.now());
        return paymentRepository.persistDto(dto);
    }

    public PaymentDto update(Integer loanId, Short paymentNumber, PaymentDto dto) {
        dto.setLoanId(loanId);
        dto.setPaymentNumber(paymentNumber);
        if (dto.getWrittenAt() == null) dto.setWrittenAt(OffsetDateTime.now());
        return paymentRepository.updateDto(dto);
    }

    public PaymentDto getById(Integer loanId, Short paymentNumber) {
        return paymentRepository.findByIdDto(loanId, paymentNumber);
    }

    public List<PaymentDto> listByLoanId(Integer loanId) {
        return paymentRepository.findByLoanId(loanId);
    }
}
