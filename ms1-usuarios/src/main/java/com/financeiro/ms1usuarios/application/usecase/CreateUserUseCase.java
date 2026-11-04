package com.financeiro.ms1usuarios.application.usecase;

import com.financeiro.ms1usuarios.application.ports.in.CreateUserInboundPort;
import com.financeiro.ms1usuarios.application.ports.out.UserRepositoryPort;
import com.financeiro.ms1usuarios.domain.exception.EmailAlreadyExistsException;
import com.financeiro.ms1usuarios.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserUseCase implements CreateUserInboundPort {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User create(User user){

        String normalizedEmail = user.getEmail().trim().toLowerCase();

        if(userRepositoryPort.findByEmail(normalizedEmail).isPresent()){
            throw new EmailAlreadyExistsException(normalizedEmail);
        }

        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return  userRepositoryPort.save(user);
    }
}
