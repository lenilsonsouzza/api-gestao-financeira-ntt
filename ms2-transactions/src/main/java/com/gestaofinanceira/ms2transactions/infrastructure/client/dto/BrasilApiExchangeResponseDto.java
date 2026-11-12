package com.gestaofinanceira.ms2transactions.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record BrasilApiExchangeResponseDto(
        @JsonProperty("cotacao_venda") BigDecimal cotacaoVenda,
        @JsonProperty("data_hora_cotacao") String dataHoraCotacao,
        @JsonProperty("tipo_boletim") String tipoBoletim
) {
}
