package com.financeiro.ms1usuarios.application.usecase;

import com.financeiro.ms1usuarios.application.ports.out.UserRepositoryPort;
import com.financeiro.ms1usuarios.domain.model.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class FindUserByIdUseCase {
    private final UserRepositoryPort userRepositoryPort;

    public Optional<User> execute(long id){
        return userRepositoryPort.findById(id);
    }
}
