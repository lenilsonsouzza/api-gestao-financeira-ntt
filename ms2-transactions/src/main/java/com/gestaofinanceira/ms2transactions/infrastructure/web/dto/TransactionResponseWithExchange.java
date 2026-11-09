package com.gestaofinanceira.ms2transactions.infrastructure.web.dto;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponseWithExchange(
        Transaction transaction,
        String targetCurrency,
        BigDecimal exchangeRate,
        BigDecimal convertedAmount,
        LocalDate rateDate,
        String message
) {}