package com.gestaofinanceira.ms2transactions.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record BalanceResponseDto(@JsonProperty("id") Long userId,
                                 @JsonProperty("saldo") BigDecimal saldo,
                                 @JsonProperty("limiteDiario") BigDecimal limiteDiario,
                                 @JsonProperty("limitePorTransacao") BigDecimal limitePorTransacao) {
}
