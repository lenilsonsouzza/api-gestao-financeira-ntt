package com.gestaofinanceira.ms2transactions.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MS2 - Microsserviço de Transações e Gestão Financeira",
                version = "1.0.0",
                description = "API RESTful do ecossistema de Gestão Financeira responsável pelo processamento de transações, conversão de câmbio, análises de despesas e relatórios.",
                contact = @Contact(
                        name = "José Lenilson",
                        email = "lenilsonsouzza@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        )
)
public class OpenApiConfig {
}