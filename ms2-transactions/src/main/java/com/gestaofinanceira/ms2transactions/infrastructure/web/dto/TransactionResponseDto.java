package com.gestaofinanceira.ms2transactions.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDto(Long id,
                                     BigDecimal amount,
                                     String type,
                                     String description,
                                     String category,
                                     Long userId,
                                     Long destinationUserId,
                                     String status,
                                     LocalDateTime timestamp,


                                     ExchangeDetailDto usdExchange,
                                     ExchangeDetailDto eurExchange) {
}
