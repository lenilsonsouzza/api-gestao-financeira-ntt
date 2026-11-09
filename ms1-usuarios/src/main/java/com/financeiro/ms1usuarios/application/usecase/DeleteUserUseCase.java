package com.financeiro.ms1usuarios.application.usecase;

import com.financeiro.ms1usuarios.application.ports.in.DeleteUserInboundPort;
import com.financeiro.ms1usuarios.application.ports.out.UserRepositoryPort;
import com.financeiro.ms1usuarios.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserUseCase implements DeleteUserInboundPort {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public void delete(Long id) {

        userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o ID: " + id));

        userRepositoryPort.deleteById(id);
    }

}
