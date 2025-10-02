package com.springonly.backend.service;

import com.springonly.backend.model.dto.LoanDTO;
import com.springonly.backend.repository.LoanRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class LoanService {

    @Inject
    LoanRepository repo;

    public List<LoanDTO> listAll() {
        return repo.listAllDTOs();
    }

    public LoanDTO getById(Integer id) {
        return repo.findDTOById(id);
    }

    public LoanDTO create(LoanDTO dto) {
        dto.setWrittenAt(OffsetDateTime.now());
        return repo.save(dto);
    }

    public LoanDTO patch(LoanDTO dto) {
        dto.setWrittenAt(OffsetDateTime.now());
        return repo.update(dto);
    }
}
