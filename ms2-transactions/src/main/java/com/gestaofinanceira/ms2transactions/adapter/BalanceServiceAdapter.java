package com.gestaofinanceira.ms2transactions.adapter;

import com.gestaofinanceira.ms2transactions.domain.ports.out.BalanceServicePort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class BalanceServiceAdapter implements BalanceServicePort {

    private final RestTemplate restTemplate;
    // URL base da nossa MockAPI (pode ser configurada no application.yml posteriormente)
    private static final String MOCK_API_URL = "https://api.mock-exemplo.com/users/";

    public BalanceServiceAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

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
        // Regra simples: A transação é permitida se o saldo for maior ou igual ao valor pretendido
        return currentBalance.compareTo(amount) >= 0;
    }
}