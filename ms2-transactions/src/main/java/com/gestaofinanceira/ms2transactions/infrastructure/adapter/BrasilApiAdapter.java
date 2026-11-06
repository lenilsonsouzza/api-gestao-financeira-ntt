package com.gestaofinanceira.ms2transactions.infrastructure.adapter;

import com.gestaofinanceira.ms2transactions.domain.ports.out.ExchangeRateServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BrasilApiAdapter implements ExchangeRateServicePort {


    private final RestTemplate restTemplate;

    @Value("${brasil.api.url}")
    private String brasilApiUrl;

    @Override
    public BigDecimal getExchangeRate(String currency) {
        String targetCurrency = currency.toUpperCase();
        try {
            String url = brasilApiUrl + targetCurrency;

            ResponseEntity response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map body = (Map) response.getBody();
                Object rateObj = body.get("valorCotacao");

                if (rateObj != null) {
                    return new BigDecimal(rateObj.toString());
                }
            }

            return getFallbackRate(targetCurrency);

        } catch (Exception e) {
            System.err.println("[WARN] Falha ao consultar BrasilAPI para a moeda " + targetCurrency + ". A utilizar taxa de fallback. Erro: " + e.getMessage());
            return getFallbackRate(targetCurrency);
        }
    }

    private BigDecimal getFallbackRate(String currency) {
        return switch (currency) {
            case "USD" -> new BigDecimal("5.50");
            case "EUR" -> new BigDecimal("6.00");
            default -> BigDecimal.ONE;
        };
    }
}