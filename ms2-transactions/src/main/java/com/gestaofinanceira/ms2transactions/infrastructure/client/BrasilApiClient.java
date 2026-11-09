package com.gestaofinanceira.ms2transactions.infrastructure.client;

import com.gestaofinanceira.ms2transactions.infrastructure.client.dto.BrasilApiExchangeResponseWrapperDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "brasilApiClient", url = "${brasil.api.url}")
public interface BrasilApiClient {

    @GetMapping("/cambio/v1/cotacao/{moeda}/{data}")
    BrasilApiExchangeResponseWrapperDto getCotacaoMoeda(
            @PathVariable("moeda") String moeda,
            @PathVariable("data") String data
    );
}