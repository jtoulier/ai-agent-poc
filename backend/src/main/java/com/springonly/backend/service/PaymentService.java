package com.springonly.backend.service;

import com.springonly.backend.model.dto.PaymentDTO;
import com.springonly.backend.repository.PaymentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PaymentService {
    @Inject
    PaymentRepository paymentRepository;
    
    public PaymentDTO createPayment(
        PaymentDTO paymentDTO
    ) {
        paymentDTO.setPaymentStateId("PENDIENTE");
        paymentDTO.setWrittenAt(OffsetDateTime.now());
        
        return paymentRepository.createPayment(paymentDTO);
    }
    
    public Optional<PaymentDTO> updatePayment(
        PaymentDTO paymentDTO
    ) {
        paymentDTO.setWrittenAt(OffsetDateTime.now());
        
        return paymentRepository.updatePayment(paymentDTO);
    }
    
    public List<PaymentDTO> listPaymentsByLoanId(
        Integer loanId
    ) {
        return paymentRepository.listPaymentsByLoanId(loanId);
    }
    
    public Optional<PaymentDTO> getPaymentById(
        Integer loanId,
        Short paymentNumber
    ) {
        return paymentRepository.getPaymentById(loanId, paymentNumber);
    }
}
