package com.financeiro.ms1usuarios.infrastructure.web.controller;

import com.financeiro.ms1usuarios.application.usecase.*;
import com.financeiro.ms1usuarios.domain.model.User;
import com.financeiro.ms1usuarios.infrastructure.web.dto.UserRequestDTO;
import com.financeiro.ms1usuarios.infrastructure.web.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    private final FindAllUsersUseCase findAllUsersUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final FindUserByEmailUseCase findUserByEmailUseCase;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserRequestDTO requestDTO) {
        User user = new User(null, requestDTO.name(), requestDTO.email(), requestDTO.password());
        User savedUser = createUserUseCase.execute(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.fromDomain(savedUser));
    }

    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<UserResponseDTO> users = findAllUsersUseCase.Execute()
                .stream()
                .map(UserResponseDTO::fromDomain).toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return findUserByIdUseCase.execute(id).map(user -> ResponseEntity.ok(UserResponseDTO.fromDomain(user))).orElse(ResponseEntity.notFound().build());

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody UserRequestDTO requestDTO) {
        User user = new User(id, requestDTO.name(), requestDTO.email(), requestDTO.password());
        User updatedUser = updateUserUseCase.execute(id, user);
        return ResponseEntity.ok(UserResponseDTO.fromDomain(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> findByEmail(@PathVariable String email) {
        return findUserByEmailUseCase.execute(email)
                .map(user -> ResponseEntity.ok(UserResponseDTO.fromDomain(user)))
                .orElse(ResponseEntity.notFound().build());
    }


}
