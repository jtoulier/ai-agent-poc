package com.springonly.backend.repository;

import com.springonly.backend.model.entity.Payment;
import com.springonly.backend.model.entity.PaymentId;
import com.springonly.backend.model.dto.PaymentDTO;
import com.springonly.backend.mapper.PaymentMapper;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PaymentRepository implements PanacheRepositoryBase<Payment, PaymentId> {

    @Inject
    PaymentMapper mapper;

    public List<PaymentDTO> listAllDTOs() {
        return listAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public PaymentDTO findDTOById(Integer loanId, Short paymentNumber) {
        Payment entity = findById(new PaymentId(loanId, paymentNumber));
        return entity != null ? mapper.toDTO(entity) : null;
    }

    public PaymentDTO save(PaymentDTO dto) {
        Payment entity = mapper.toEntity(dto);
        if (entity.getWrittenAt() == null) {
            entity.setWrittenAt(OffsetDateTime.now());
        }
        persist(entity);
        return mapper.toDTO(entity);
    }

    public PaymentDTO update(PaymentDTO dto) {
        Payment entity = mapper.toEntity(dto);
        entity.setWrittenAt(OffsetDateTime.now());
        getEntityManager().merge(entity);
        return mapper.toDTO(entity);
    }
}
