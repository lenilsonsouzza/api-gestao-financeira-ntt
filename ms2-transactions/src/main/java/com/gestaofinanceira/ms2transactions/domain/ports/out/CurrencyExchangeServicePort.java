package com.gestaofinanceira.ms2transactions.domain.ports.out;

import java.math.BigDecimal;

public interface CurrencyExchangeServicePort {
    BigDecimal getExchangeRate(String currency);
}
