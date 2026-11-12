package com.gestaofinanceira.ms2transactions.infrastructure.adapter;

import com.gestaofinanceira.ms2transactions.domain.model.Cotacao;
import com.gestaofinanceira.ms2transactions.domain.ports.out.ExchangeRateServicePort;
import com.gestaofinanceira.ms2transactions.infrastructure.client.BrasilApiClient;
import com.gestaofinanceira.ms2transactions.infrastructure.client.dto.BrasilApiExchangeResponseWrapperDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BrasilApiAdapter implements ExchangeRateServicePort {

    private final BrasilApiClient brasilApiClient;

    @Override
    public Optional<Cotacao> buscar(String moeda) {
        String targetCurrency = moeda.toUpperCase();


        LocalDate dataBase = LocalDate.now().minusDays(1);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        for (int i = 0; i < 5; i++) {
            LocalDate dataConsulta = dataBase.minusDays(i);
            String formattedDate = dataConsulta.format(formatter);

            try {
                log.info("A consultar cotação para {} na data {}", targetCurrency, formattedDate);
                BrasilApiExchangeResponseWrapperDto response = brasilApiClient.getCotacaoMoeda(targetCurrency, formattedDate);

                if (response != null && response.cotacoes() != null && !response.cotacoes().isEmpty()) {

                    var ultimaCotacaoDto = response.cotacoes().get(response.cotacoes().size() - 1);
                    BigDecimal cotacaoVenda = ultimaCotacaoDto.cotacaoVenda();

                    if (cotacaoVenda != null) {
                        return Optional.of(new Cotacao(targetCurrency, cotacaoVenda, dataConsulta));
                    }
                }
            } catch (FeignException.NotFound | FeignException.BadRequest e) {

                log.warn("Cotação indisponível para a data {} (Erro {}). A tentar o dia anterior...", formattedDate, e.status());
            } catch (Exception e) {
                log.error("Erro crítico de comunicação com a BrasilAPI para a data {}: {}", formattedDate, e.getMessage());
                return Optional.empty();
            }
        }

        return Optional.empty();
    }
}