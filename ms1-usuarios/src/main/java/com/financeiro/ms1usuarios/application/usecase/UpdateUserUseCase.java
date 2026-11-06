package com.financeiro.ms1usuarios.application.usecase;

import com.financeiro.ms1usuarios.application.ports.in.UpdateUserInboundPort;
import com.financeiro.ms1usuarios.application.ports.out.UserRepositoryPort;
import com.financeiro.ms1usuarios.domain.model.User;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UpdateUserUseCase implements UpdateUserInboundPort {
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User update(Long id, User updateUser) {
        User existingUser = userRepositoryPort.findById(id).orElseThrow(() -> new RuntimeException("Usuario não encontrado com o ID: " + id));
       String newEmail = updateUser.getEmail().trim().toLowerCase();
        Optional<User> userWithSameEmail = userRepositoryPort.findByEmail(newEmail);
       if(userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(id)){
           throw new DuplicateRequestException("O e-mail informado já está em uso por outro Usuário.");
       }
       String encodePassword = existingUser.getPassword();
       if(updateUser.getPassword()!= null && !updateUser.getPassword().isBlank()){
           encodePassword=passwordEncoder.encode(updateUser.getPassword());
       }

        User userTosave = new User(
                existingUser.getId(),
                updateUser.getName(),
                newEmail,
                encodePassword
        );
        return userRepositoryPort.save(userTosave);
    }
}
