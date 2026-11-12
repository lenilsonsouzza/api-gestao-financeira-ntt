package com.gestaofinanceira.ms2transactions.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExchangeDetailDto (BigDecimal convertedAmount,
                                BigDecimal exchangeRate,
                                LocalDate rateDate,
                                String message)
{}
