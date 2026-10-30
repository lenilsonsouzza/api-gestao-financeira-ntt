package com.financeiro.ms1usuarios.application.usecase;

import com.financeiro.ms1usuarios.application.ports.out.UserRepositoryPort;
import com.financeiro.ms1usuarios.domain.model.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateUserUseCase {
    private final UserRepositoryPort userRepositoryPort;

    public User execute(Long id, User updateUser) {
        User existingUser = userRepositoryPort.findById(id).orElseThrow(() -> new RuntimeException("Usuario não encontrado com o ID: " + id));
        User userTosave = new User(
                existingUser.getId(),
                updateUser.getName(),
                updateUser.getEmail(),
                updateUser.getPassword()
        );
        return userRepositoryPort.save(userTosave);
    }
}
