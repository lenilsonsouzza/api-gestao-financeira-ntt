package com.gestaofinanceira.ms2transactions.domain.usecase;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;
import com.gestaofinanceira.ms2transactions.domain.ports.out.ExchangeRateServicePort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.TransactionRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
public class GetTransactionWithExchangeUseCase {

    private final TransactionRepositoryPort transactionRepositoryPort;
    private final ExchangeRateServicePort exchangeRateServicePort;

    public TransactionResponseWithExchange execute(Long transactionId, String targetCurrency) {
        Transaction transaction = transactionRepositoryPort.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada com ID: " + transactionId));

        BigDecimal exchangeRate = exchangeRateServicePort.getExchangeRate(targetCurrency);

        // Calcula o valor convertido (Valor da Transação / Taxa de Câmbio)
        BigDecimal convertedAmount = transaction.getAmount()
                .divide(exchangeRate, 2, RoundingMode.HALF_UP);

        return new TransactionResponseWithExchange(
                transaction,
                targetCurrency,
                exchangeRate,
                convertedAmount
        );
    }
}