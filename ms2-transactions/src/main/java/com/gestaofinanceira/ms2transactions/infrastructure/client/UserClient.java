package com.gestaofinanceira.ms2transactions.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms1-users", url = "${ms1.service.url:http://localhost:8080}")
public interface UserClient {

    @GetMapping("/api/users/{id}")
    Object getUserById(@PathVariable("id") Long id);
}