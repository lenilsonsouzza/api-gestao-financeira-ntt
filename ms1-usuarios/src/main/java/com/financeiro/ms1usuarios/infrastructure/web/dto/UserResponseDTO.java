package com.financeiro.ms1usuarios.infrastructure.web.dto;

import com.financeiro.ms1usuarios.domain.model.User;

public record UserResponseDTO(Long id, String name, String email) {

    public static UserResponseDTO fromDomain(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());

    }
}