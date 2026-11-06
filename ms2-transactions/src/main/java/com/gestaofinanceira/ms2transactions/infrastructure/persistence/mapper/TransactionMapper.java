package com.gestaofinanceira.ms2transactions.infrastructure.persistence.mapper;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;
import com.gestaofinanceira.ms2transactions.domain.model.TransactionStatus;
import com.gestaofinanceira.ms2transactions.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionEntity toEntity(Transaction domain) {
        if (domain == null) {
            return null;
        }

        TransactionEntity entity = new TransactionEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setType(domain.getType());
        entity.setAmount(domain.getAmount());
        entity.setTimestamp(domain.getTimestamp());
        entity.setDescription(domain.getDescription());
        entity.setCategory(domain.getCategory());
        entity.setDestinationUserId(domain.getDestinationUserId());
        entity.setStatus(domain.getStatus());
        entity.setRejectionReason(domain.getRejectionReason());
        return entity;
    }

    public Transaction toDomain(TransactionEntity entity) {
        if (entity == null) {
            return null;
        }

        Transaction domain = new Transaction(
                entity.getId(),
                entity.getUserId(),
                entity.getType(),
                entity.getAmount(),
                entity.getTimestamp(),
                entity.getDescription(),
                entity.getCategory(),
                entity.getDestinationUserId(),
                entity.getStatus(),
                entity.getRejectionReason()
        );

        return domain;
    }
}