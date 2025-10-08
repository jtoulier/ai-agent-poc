package com.springonly.backend.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import com.springonly.backend.model.entity.PaymentEntity;
import com.springonly.backend.model.entity.PaymentId;
import com.springonly.backend.model.dto.PaymentDto;
import com.springonly.backend.mapper.PaymentMapper;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PaymentRepository implements PanacheRepositoryBase<PaymentEntity, PaymentId> {

    @Inject
    PaymentMapper paymentMapper;

    public PaymentDto persistDto(PaymentDto dto) {
        PaymentEntity e = paymentMapper.toEntity(dto);
        persist(e);
        return paymentMapper.toDto(e);
    }

    public PaymentDto updateDto(PaymentDto dto) {
        PaymentId id = new PaymentId(dto.getLoanId(), dto.getPaymentNumber());
        PaymentEntity e = findById(id);
        if (e == null) return persistDto(dto);
        e.setDueDate(dto.getDueDate());
        e.setPrincipalAmount(dto.getPrincipalAmount());
        e.setInterestAmount(dto.getInterestAmount());
        e.setPaymentStateId(dto.getPaymentStateId());
        e.setWrittenAt(dto.getWrittenAt());
        persist(e);
        return paymentMapper.toDto(e);
    }

    public List<PaymentDto> findByLoanId(Integer loanId) {
        return find("id.loanId", loanId)
                .stream()
                .map(e -> {
                    PaymentDto dto = paymentMapper.toDto(e);
                    dto.setLoanId(e.getId().getLoanId());
                    dto.setPaymentNumber(e.getId().getPaymentNumber());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public PaymentDto findByIdDto(Integer loanId, Short paymentNumber) {
        PaymentId id = new PaymentId(loanId, paymentNumber);
        PaymentEntity e = findById(id);
        if (e == null) return null;

        PaymentDto dto = paymentMapper.toDto(e);
        // Asegurar que los IDs compuestos estén seteados correctamente
        dto.setLoanId(loanId);
        dto.setPaymentNumber(paymentNumber);
        return dto;
    }

}
