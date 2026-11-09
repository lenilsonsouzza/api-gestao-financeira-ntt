package com.gestaofinanceira.ms2transactions.infrastructure.web.dto;

import com.gestaofinanceira.ms2transactions.domain.model.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionRequestDto(
        @NotNull(message = "O ID do Usuário é obrigatório.")
        Long userId,

        @NotNull(message = "O tipo de transação é obrigatório.")
        TransactionType type,

        @NotNull(message = "O valor da transação é obrigatório.")
        @Positive(message = "O valor da transação deve ser superior a zero.")
        BigDecimal amount,

        String description,
        String category,
        Long destinationUserId
) {
}