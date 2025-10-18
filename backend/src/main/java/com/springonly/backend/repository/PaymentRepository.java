package com.springonly.backend.repository;

import com.springonly.backend.mapper.PaymentMapper;
import com.springonly.backend.model.dto.PaymentDTO;
import com.springonly.backend.model.entity.PaymentEntity;
import com.springonly.backend.model.entity.PaymentId;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PaymentRepository implements PanacheRepositoryBase<PaymentEntity, PaymentId> {
    @Inject
    PaymentMapper paymentMapper;
    
    // ✅ Crear un nuevo pago
    public PaymentDTO createPayment(
        PaymentDTO paymentDTO
    ) {
        // Construir el ID compuesto con el correlativo correcto
        PaymentId id = new PaymentId(
                            paymentDTO.getLoanId(),
                            paymentDTO.getPaymentNumber()
                        );
        
        // Mapear DTO a entidad y asignar el ID calculado
        PaymentEntity entity = paymentMapper.fromDTOToEntity(paymentDTO);
        entity.setId(id);
        
        persist(entity);
        
        return paymentMapper.fromEntityToDTO(entity);
    }

    
    // ✅ Actualizar un pago existente
    public Optional<PaymentDTO> updatePayment(
        PaymentDTO paymentDTO
    ) {
        if (paymentDTO.getLoanId() == null || paymentDTO.getPaymentNumber() == null) {
            // No se puede buscar el registro sin ambos identificadores
            return Optional.empty();
        }
        
        PaymentId paymentId = new PaymentId(
                                    paymentDTO.getLoanId(),
                                    paymentDTO.getPaymentNumber()
                                );
        
        PaymentEntity existing = findById(paymentId);
        
        if (existing == null) {
            return Optional.empty();
        }
        
        // Actualizar campos relevantes
        existing.setPaymentStateId(paymentDTO.getPaymentStateId());
        existing.setWrittenAt(paymentDTO.getWrittenAt());
        
        // Hibernate sincroniza automáticamente al final de la transacción
        return Optional
                .of(paymentMapper.fromEntityToDTO(existing));
    }
    
    // ✅ Listar todos los pagos de un préstamo
    public List<PaymentDTO> listPaymentsByLoanId(
        Integer loanId
    ) {
        List<PaymentEntity> entities =
            list("id.loanId", loanId);
        
        return entities
                .stream()
                .map(paymentMapper::fromEntityToDTO)
                .toList();
    }
    
    // ✅ Obtener un pago específico por LoanId + PaymentNumber
    public Optional<PaymentDTO> getPaymentById(
        Integer loanId,
        Short paymentNumber
    ) {
        PaymentId id = new PaymentId(loanId, paymentNumber);
        PaymentEntity entity = findById(id);
        return Optional
                .ofNullable(paymentMapper.fromEntityToDTO(entity));
    }
}