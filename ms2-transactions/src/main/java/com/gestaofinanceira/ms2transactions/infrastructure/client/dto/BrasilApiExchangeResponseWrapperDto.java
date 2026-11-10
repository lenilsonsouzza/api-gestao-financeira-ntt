package com.gestaofinanceira.ms2transactions.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record BrasilApiExchangeResponseWrapperDto(
        @JsonProperty("cotacoes") List<BrasilApiExchangeResponseDto> cotacoes,
        @JsonProperty("moeda") String moeda,
        @JsonProperty("data") String data
) {}