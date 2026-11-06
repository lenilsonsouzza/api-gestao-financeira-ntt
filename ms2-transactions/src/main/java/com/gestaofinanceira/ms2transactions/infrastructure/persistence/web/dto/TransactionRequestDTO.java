package com.gestaofinanceira.ms2transactions.infrastructure.persistence.web.dto;

import com.gestaofinanceira.ms2transactions.domain.model.TransactionType;

import java.math.BigDecimal;

public record TransactionRequestDTO(
        Long userId,
        TransactionType type,
        BigDecimal amount,
        String description,
        String category,
        Long destinationUserId
) {
}
