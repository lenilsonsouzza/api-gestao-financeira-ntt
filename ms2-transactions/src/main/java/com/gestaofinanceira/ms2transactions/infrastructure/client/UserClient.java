package com.gestaofinanceira.ms2transactions.infrastructure.client;

import com.gestaofinanceira.ms2transactions.infrastructure.client.dto.UserResponseDto;
import com.gestaofinanceira.ms2transactions.infrastructure.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms1-users",
        url = "${ms1.service.url:http://localhost:8080}",
        configuration = FeignClientConfig.class // <-- Aplica o interceptor de segurança aqui
)
public interface UserClient {

    @GetMapping("/api/users/{id}")
    UserResponseDto getUserById(@PathVariable("id") Long id);
}