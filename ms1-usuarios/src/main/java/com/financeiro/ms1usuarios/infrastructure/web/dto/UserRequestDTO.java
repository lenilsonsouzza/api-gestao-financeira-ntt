package com.financeiro.ms1usuarios.infrastructure.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "O nome é obrigátorio")
        String name,
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de Email invalido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
        String password) {

}
