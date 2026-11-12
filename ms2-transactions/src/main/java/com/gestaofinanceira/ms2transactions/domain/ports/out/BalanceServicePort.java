package com.gestaofinanceira.ms2transactions.domain.ports.out;

import com.gestaofinanceira.ms2transactions.infrastructure.client.dto.BalanceResponseDto;

import java.math.BigDecimal;

public interface BalanceServicePort {
    BigDecimal getBalance(Long userId);
    boolean validateLimit(Long userId, BigDecimal amount);
    BalanceResponseDto getBalanceData(Long userId);
    void debit(Long userId, BigDecimal amount);
    void credit(Long userId, BigDecimal amount);

}
