package com.financeiro.ms1usuarios.application.usecase;

import com.financeiro.ms1usuarios.application.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteUserUseCase {
    private final UserRepositoryPort userRepositoryPort;

    public void execute(Long id){
        if(userRepositoryPort.findById(id).isEmpty()){
            throw new RuntimeException("Usuario não encontrado com o ID "+ id);
        }
        userRepositoryPort.deletaById(id);
    }

}
