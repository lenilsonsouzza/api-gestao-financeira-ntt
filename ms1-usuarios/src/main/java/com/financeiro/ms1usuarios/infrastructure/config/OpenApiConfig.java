package com.financeiro.ms1usuarios.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI(){
        return  new OpenAPI().info(new Info().title("MS1 - Microsserviço de Gestão de Usuários")
                .description("API RESTful do ecossistema de Gestão Financeira responsável pelo Cadastro,gestão e autenticação de usuários.")
                .version("1.0.0")
                .contact(new Contact()
                        .name("José Lenilson")
                        .email("lenilsonsouzza@gmail.com"))
                .license( new License()
                        .name("Apache 2.0")
                        .url("https://springdoc.org")));
    }
}
