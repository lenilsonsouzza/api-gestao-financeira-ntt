package com.financeiro.ms1usuarios.application.usecase;

import com.financeiro.ms1usuarios.application.ports.out.UserRepositoryPort;
import com.financeiro.ms1usuarios.domain.exception.EmailAlreadyExistsException;
import com.financeiro.ms1usuarios.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;


    public User execute(User user){
        if(userRepositoryPort.findByEmail(user.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return  userRepositoryPort.save(user);
    }
}
