package com.gestaofinanceira.ms2transactions.infrastructure.adapter;

import com.gestaofinanceira.ms2transactions.domain.ports.out.BalanceServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class BalanceServiceAdapter implements BalanceServicePort {

    private final RestTemplate restTemplate;

    @Value("${app.client.mock-api-url}")
    private String mockApiUrl;


    @Override
    public BigDecimal getBalance(Long userId) {
        try {
            // Exemplo de chamada HTTP síncrona para o serviço de saldo do MS1 ou MockAPI
            // String url = MOCK_API_URL + userId + "/balance";
            // return restTemplate.getForObject(url, BigDecimal.class);

            // Mock temporário para validação inicial de desenvolvimento
            return new BigDecimal("1000.00");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar saldo do utilizador: " + userId, e);
        }
    }

    @Override
    public boolean validateLimit(Long userId, BigDecimal amount) {
        BigDecimal currentBalance = getBalance(userId);

        return currentBalance.compareTo(amount) >= 0;
    }
}