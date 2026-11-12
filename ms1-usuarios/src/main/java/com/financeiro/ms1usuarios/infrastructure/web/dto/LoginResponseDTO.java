package com.financeiro.ms1usuarios.infrastructure.web.dto;

public record LoginResponseDTO(String token, String type) {
    public LoginResponseDTO(String token) {
        this(token, "Bearer");
    }
}
