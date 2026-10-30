package com.financeiro.ms1usuarios.application.ports.out;

import com.financeiro.ms1usuarios.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);

}
