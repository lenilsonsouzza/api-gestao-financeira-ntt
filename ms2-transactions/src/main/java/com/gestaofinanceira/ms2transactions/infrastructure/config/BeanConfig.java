package com.gestaofinanceira.ms2transactions.infrastructure.config;

import com.gestaofinanceira.ms2transactions.domain.ports.out.BalanceServicePort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.ExchangeRateServicePort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.TransactionRepositoryPort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.UserValidationPort;
import com.gestaofinanceira.ms2transactions.domain.usecase.CreateTransactionUseCase;
import com.gestaofinanceira.ms2transactions.domain.usecase.GetTransactionWithExchangeUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CreateTransactionUseCase createTransactionUseCase(
            TransactionRepositoryPort transactionRepositoryPort,
            BalanceServicePort balanceServicePort,
            UserValidationPort userValidationPort) {
        return new CreateTransactionUseCase(transactionRepositoryPort, balanceServicePort, userValidationPort);
    }

    @Bean
    public GetTransactionWithExchangeUseCase getTransactionWithExchangeUseCase(
            TransactionRepositoryPort transactionRepositoryPort,
            ExchangeRateServicePort exchangeRateServicePort) {
        return new GetTransactionWithExchangeUseCase(transactionRepositoryPort, exchangeRateServicePort);
    }
}