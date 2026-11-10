package com.gestaofinanceira.ms2transactions.domain.usecase;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;
import com.gestaofinanceira.ms2transactions.domain.model.TransactionStatus;
import com.gestaofinanceira.ms2transactions.domain.ports.out.ExchangeRateServicePort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.TransactionRepositoryPort;
import com.gestaofinanceira.ms2transactions.infrastructure.web.dto.ExchangeDetailDto;
import com.gestaofinanceira.ms2transactions.infrastructure.web.dto.TransactionResponseWithExchange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetTransactionWithExchangeUseCase {

    private final TransactionRepositoryPort transactionRepositoryPort;
    private final ExchangeRateServicePort exchangeRateServicePort;

    public TransactionResponseWithExchange execute(Long transactionId) {
        // 1. Busca a transação pelo ID
        Transaction transaction = transactionRepositoryPort.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada com o ID: " + transactionId));

        List<ExchangeDetailDto> conversoes = new ArrayList<>();
        String aviso = null;

        // 2. Valida se a transação está APROVADA antes de buscar o câmbio
        if (TransactionStatus.APPROVED.equals(transaction.getStatus())) {
            for (String moeda : List.of("USD", "EUR")) {
                try {
                    var cotacaoOpt = exchangeRateServicePort.buscar(moeda);

                    cotacaoOpt.ifPresent(c -> {
                        BigDecimal convertedAmount = transaction.getAmount()
                                .divide(c.taxa(), 2, RoundingMode.HALF_UP);

                        conversoes.add(new ExchangeDetailDto(
                                convertedAmount,
                                c.taxa(),
                                c.data(),
                                moeda
                        ));
                    });
                } catch (Exception e) {
                    log.warn("Falha ao obter cotação para a moeda {}: {}", moeda, e.getMessage());
                }
            }

            aviso = conversoes.size() < 2 ? "Conversão indisponível no momento" : null;
        } else {
            aviso = "Conversão não aplicável para transações não aprovadas.";
        }

        return new TransactionResponseWithExchange(transaction, conversoes, aviso);
    }
}