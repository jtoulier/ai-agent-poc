package com.springonly.backend.service;

import com.springonly.backend.model.dto.PaymentDTO;
import com.springonly.backend.repository.PaymentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class PaymentService {

    @Inject
    PaymentRepository repo;

    public List<PaymentDTO> listAll() {
        return repo.listAllDTOs();
    }

    public PaymentDTO getById(Integer loanId, Short paymentNumber) {
        return repo.findDTOById(loanId, paymentNumber);
    }

    public PaymentDTO create(PaymentDTO dto) {
        dto.setWrittenAt(OffsetDateTime.now());
        return repo.save(dto);
    }

    public PaymentDTO patch(PaymentDTO dto) {
        dto.setWrittenAt(OffsetDateTime.now());
        return repo.update(dto);
    }
}
