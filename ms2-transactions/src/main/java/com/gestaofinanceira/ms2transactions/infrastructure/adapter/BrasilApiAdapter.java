package com.gestaofinanceira.ms2transactions.infrastructure.adapter;

import com.gestaofinanceira.ms2transactions.domain.ports.out.ExchangeRateServicePort;
import com.gestaofinanceira.ms2transactions.infrastructure.client.BrasilApiClient;
import com.gestaofinanceira.ms2transactions.infrastructure.client.dto.BrasilApiExchangeResponseWrapperDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class BrasilApiAdapter implements ExchangeRateServicePort {

    private final BrasilApiClient brasilApiClient;

    @Override
    public BigDecimal getExchangeRate(String currency) {
        String targetCurrency = currency.toUpperCase();
        LocalDate date = LocalDate.now();

        for (int i = 0; i < 5; i++) {
            String formattedDate = date.minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            try {
                log.info("A consultar cotação para {} na data {}", targetCurrency, formattedDate);
                BrasilApiExchangeResponseWrapperDto response = brasilApiClient.getCotacaoMoeda(targetCurrency, formattedDate);

                if (response != null && response.cotacoes() != null && !response.cotacoes().isEmpty()) {
                    BigDecimal cotacaoVenda = response.cotacoes().get(0).cotacaoVenda();
                    if (cotacaoVenda != null) {
                        return cotacaoVenda;
                    }
                }
            } catch (Exception e) {
                log.warn("Tentativa falhou para a data {}: {}", formattedDate, e.getMessage());
            }
        }

        throw new IllegalArgumentException("Não foi possível encontrar cotação válida para a moeda: " + targetCurrency);
    }
}