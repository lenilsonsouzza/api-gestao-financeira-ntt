package com.gestaofinanceira.ms2transactions.domain.ports.out;

import java.math.BigDecimal;

public interface BalanceServicePort {
    BigDecimal getBalance(Long userId);
    boolean validateLimit(Long userId, BigDecimal amount);

}
