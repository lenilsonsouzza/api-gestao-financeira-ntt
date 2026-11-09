package com.gestaofinanceira.ms2transactions.infrastructure.client;

import com.gestaofinanceira.ms2transactions.infrastructure.client.dto.BalanceResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "balanceMockClient", url = "${app.client.mock-api-url}")
public interface BalanceMockClient {

    @GetMapping("/users/{userId}")
    BalanceResponseDto getBalanceAndLimits(@PathVariable("userId") Long userId);
}