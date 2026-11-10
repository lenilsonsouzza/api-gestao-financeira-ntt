package com.gestaofinanceira.ms2transactions.infrastructure.web.dto;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;

import java.util.List;

public record TransactionResponseWithExchange(
        Transaction transaction,
        List<ExchangeDetailDto> conversoes,
        String message
) {}