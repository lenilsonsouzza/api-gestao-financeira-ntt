package com.gestaofinanceira.ms2transactions.domain.ports.out;

import java.math.BigDecimal;

public interface ExchangeRateServicePort {
    BigDecimal getExchangeRate(String currency);
}
