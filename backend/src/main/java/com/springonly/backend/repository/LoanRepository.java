package com.springonly.backend.repository;

import com.springonly.backend.mapper.LoanMapper;
import com.springonly.backend.model.dto.LoanDTO;
import com.springonly.backend.model.entity.LoanEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LoanRepository implements PanacheRepositoryBase<LoanEntity, Integer> {
    @Inject
    LoanMapper loanMapper;
    
    public List<LoanDTO> listLoansByCustomerId(
        String customerId
    ) {
        // 1️⃣ Ejecutamos el SELECT con Panache (JPQL simplificado)
        List<LoanEntity> loanEntities =
            find("customerId", customerId)
            .list();
        
        // 2️⃣ Convertimos cada entidad a DTO con el mapper
        return loanEntities
                .stream()
                .map(loanMapper::fromEntityToDTO)
                .toList();
    }
    
    public LoanDTO createLoan(
        LoanDTO loanDTO
    ) {
        // 1️⃣ Convertimos el DTO a entidad
        LoanEntity loanEntity = loanMapper.fromDTOToEntity(loanDTO);
        
        // 2️⃣ Persistimos con Panache (INSERT)
        persist(loanEntity);
        
        // 3️⃣ Retornamos el DTO recién creado
        return loanMapper.fromEntityToDTO(loanEntity);
    }
    
    public Optional<LoanDTO> updateLoan(
        LoanDTO loanDTO
    ) {
        return Optional
                .ofNullable(
                    findById(loanDTO.getLoanId())
                )
                .map(
                    existingLoan
                    ->
                    {
                        existingLoan.setLoanStateId(loanDTO.getLoanStateId());
                        existingLoan.setWrittenAt(loanDTO.getWrittenAt());
                        
                        return loanMapper.fromEntityToDTO(existingLoan);
                    }
                );
    }
    
    public Optional<LoanDTO> getLoanById(
        Integer loanId
    ) {
        // 1️⃣ Buscamos por ID
        LoanEntity loanEntity = findById(loanId);
        
        // 2️⃣ Convertimos a DTO si existe
        return Optional
                .ofNullable(loanEntity)
                .map(loanMapper::fromEntityToDTO);
    }
    
}
