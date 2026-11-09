package com.gestaofinanceira.ms2transactions.infrastructure.adapter;

import com.gestaofinanceira.ms2transactions.domain.ports.out.BalanceServicePort;
import com.gestaofinanceira.ms2transactions.infrastructure.client.BalanceMockClient;
import com.gestaofinanceira.ms2transactions.infrastructure.client.dto.BalanceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class BalanceServiceAdapter implements BalanceServicePort {

    private final BalanceMockClient balanceMockClient;

    @Override
    public BigDecimal getBalance(Long userId) {
        try {
            BalanceResponseDto response = balanceMockClient.getBalanceAndLimits(userId);
            if (response == null || response.saldo() == null) {
                throw new IllegalStateException("Resposta inválida da API Mock de saldo.");
            }
            return response.saldo();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Serviço de saldo indisponível no momento");
        }
    }

    @Override
    public boolean validateLimit(Long userId, BigDecimal amount) {
        try {
            BalanceResponseDto response = balanceMockClient.getBalanceAndLimits(userId);
            if (response == null || response.limitePorTransacao() == null) {
                return false;
            }
            return amount.compareTo(response.limitePorTransacao()) <= 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Serviço de limite indisponível no momento");
        }
    }

    @Override
    public BalanceResponseDto getBalanceData(Long userId) {
        try {
            BalanceResponseDto response = balanceMockClient.getBalanceAndLimits(userId);
            if (response == null) {
                throw new IllegalStateException("Resposta inválida da API Mock de saldo.");
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Serviço de saldo indisponível no momento");
        }
    }
}