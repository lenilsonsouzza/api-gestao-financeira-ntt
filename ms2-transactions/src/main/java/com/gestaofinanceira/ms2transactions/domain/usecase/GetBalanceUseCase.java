package com.gestaofinanceira.ms2transactions.domain.usecase;

import com.gestaofinanceira.ms2transactions.domain.ports.out.BalanceServicePort;
import com.gestaofinanceira.ms2transactions.infrastructure.client.dto.BalanceResponseDto;

import java.math.BigDecimal;

public class GetBalanceUseCase {

    private final BalanceServicePort balanceServicePort;

    public GetBalanceUseCase(BalanceServicePort balanceServicePort) {
        this.balanceServicePort = balanceServicePort;
    }

    public BalanceResponseDto execute(Long userId) {
        return balanceServicePort.getBalanceData(userId);
    }
}
