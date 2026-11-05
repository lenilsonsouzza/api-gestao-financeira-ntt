package com.gestaofinanceira.ms2transactions.domain.usecase;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;

import java.math.BigDecimal;

public record TransactionResponseWithExchange(
        Transaction transaction,
        String targetCurrency,
        BigDecimal exchangeRate,
        BigDecimal convertedAmount
) {}