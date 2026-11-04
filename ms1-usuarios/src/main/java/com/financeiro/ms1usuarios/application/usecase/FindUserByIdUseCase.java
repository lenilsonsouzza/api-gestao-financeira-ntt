package com.financeiro.ms1usuarios.application.usecase;

import com.financeiro.ms1usuarios.application.ports.in.FindUserByIdInboundPort;
import com.financeiro.ms1usuarios.application.ports.out.UserRepositoryPort;
import com.financeiro.ms1usuarios.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FindUserByIdUseCase implements FindUserByIdInboundPort {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Optional<User> findById(Long id){
        return userRepositoryPort.findById(id);
    }
}
