package com.springonly.backend.repository;

import com.springonly.backend.model.entity.Loan;
import com.springonly.backend.model.dto.LoanDTO;
import com.springonly.backend.mapper.LoanMapper;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LoanRepository implements PanacheRepositoryBase<Loan, Integer> {

    @Inject
    LoanMapper mapper;

    public List<LoanDTO> listAllDTOs() {
        return listAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public LoanDTO findDTOById(Integer id) {
        Loan entity = findById(id);
        return entity != null ? mapper.toDTO(entity) : null;
    }

    public LoanDTO save(LoanDTO dto) {
        Loan entity = mapper.toEntity(dto);
        if (entity.getWrittenAt() == null) {
            entity.setWrittenAt(OffsetDateTime.now());
        }
        persist(entity);
        return mapper.toDTO(entity);
    }

    public LoanDTO update(LoanDTO dto) {
        Loan entity = mapper.toEntity(dto);
        entity.setWrittenAt(OffsetDateTime.now());
        getEntityManager().merge(entity);
        return mapper.toDTO(entity);
    }
}
