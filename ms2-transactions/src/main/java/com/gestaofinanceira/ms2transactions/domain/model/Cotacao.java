package com.gestaofinanceira.ms2transactions.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Cotacao(String moeda,
                      BigDecimal taxa,
                      LocalDate data) {
}
