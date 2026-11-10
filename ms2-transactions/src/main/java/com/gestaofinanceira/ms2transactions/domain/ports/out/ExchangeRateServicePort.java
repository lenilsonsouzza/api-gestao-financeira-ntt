package com.gestaofinanceira.ms2transactions.domain.ports.out;

import com.gestaofinanceira.ms2transactions.domain.model.Cotacao;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExchangeRateServicePort {
    Optional<Cotacao> buscar(String moeda);
}
