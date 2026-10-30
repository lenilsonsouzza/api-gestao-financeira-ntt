package com.financeiro.ms1usuarios.application.usecase;

import com.financeiro.ms1usuarios.application.ports.out.UserRepositoryPort;
import com.financeiro.ms1usuarios.domain.model.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FindAllUsersUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public List<User> Execute(){
        return userRepositoryPort.findAll();
    }
}
