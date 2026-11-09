package com.gestaofinanceira.ms2transactions.domain.usecase;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;
import com.gestaofinanceira.ms2transactions.domain.ports.out.ExchangeRateServicePort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.TransactionRepositoryPort;

import com.gestaofinanceira.ms2transactions.infrastructure.web.dto.TransactionResponseWithExchange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetTransactionWithExchangeUseCase {

    private final TransactionRepositoryPort transactionRepositoryPort;

    private final ExchangeRateServicePort exchangeRateServicePort;

    public TransactionResponseWithExchange execute(Long transactionId, String targetCurrency) {

        Transaction transaction = transactionRepositoryPort.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada com o ID: " + transactionId));

        String currency = targetCurrency.toUpperCase();

        try {

            BigDecimal exchangeRate = exchangeRateServicePort.getExchangeRate(currency);


            BigDecimal convertedAmount = transaction.getAmount()
                    .divide(exchangeRate, 2, RoundingMode.HALF_UP);


            return new TransactionResponseWithExchange(
                    transaction,
                    currency,
                    exchangeRate,
                    convertedAmount,
                    LocalDate.now(),
                    null
            );

        } catch (Exception e) {
            // 5. Fallback gracioso: Se a BrasilAPI falhar, loga o aviso mas NÃO quebra a consulta da transação
            log.warn("BrasilAPI indisponível ou falhou para a moeda {}: {}", currency, e.getMessage());

            return new TransactionResponseWithExchange(
                    transaction,
                    currency,
                    null,
                    null,
                    null,
                    "Conversão indisponível no momento"
            );
        }
    }
}