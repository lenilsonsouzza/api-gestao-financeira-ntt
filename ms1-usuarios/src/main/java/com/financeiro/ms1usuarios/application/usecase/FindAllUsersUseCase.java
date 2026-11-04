package com.financeiro.ms1usuarios.application.usecase;

import com.financeiro.ms1usuarios.application.ports.in.FindAllUsersInboundPort;
import com.financeiro.ms1usuarios.application.ports.out.UserRepositoryPort;
import com.financeiro.ms1usuarios.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
@RequiredArgsConstructor
public class FindAllUsersUseCase implements FindAllUsersInboundPort{

    private final UserRepositoryPort userRepositoryPort;

    public List<User> findAll() {
        return userRepositoryPort.findAll();
    }
}
