package com.financeiro.ms1usuarios.infrastructure.web.controller;

import com.financeiro.ms1usuarios.application.usecase.*;
import com.financeiro.ms1usuarios.domain.model.User;
import com.financeiro.ms1usuarios.infrastructure.web.dto.UserRequestDTO;
import com.financeiro.ms1usuarios.infrastructure.web.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuarios",description = "endpoints para gestão e autenticação de Usuários")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final FindAllUsersUseCase findAllUsersUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final ExcluirUserUseCase deleteUserUseCase;
    private final FindUserByEmailUseCase findUserByEmailUseCase;

    @Operation(summary = "Cadastrar um Novo Usuário",description = "Realiza o Cadastro de um Usuário")
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserRequestDTO requestDTO) {
        User user = new User(null, requestDTO.name(), requestDTO.email(), requestDTO.password());
        User savedUser = createUserUseCase.execute(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.fromDomain(savedUser));
    }

    @Operation(summary = "Buscar todos os usuário ", description = "Retorna os detalhes de todos Usuários.")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<UserResponseDTO> users = findAllUsersUseCase.Execute()
                .stream()
                .map(UserResponseDTO::fromDomain).toList();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna os detalhes de um Usuário específico.")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return findUserByIdUseCase.execute(id).map(user -> ResponseEntity.ok(UserResponseDTO.fromDomain(user))).orElse(ResponseEntity.notFound().build());

    }

    @Operation(summary = "Alterar usuário por ID", description = "Altera os dados de um Usuário específico.")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody @Valid UserRequestDTO requestDTO) {
        User user = new User(id, requestDTO.name(), requestDTO.email(), requestDTO.password());
        User updatedUser = updateUserUseCase.execute(id, user);
        return ResponseEntity.ok(UserResponseDTO.fromDomain(updatedUser));
    }
    @Operation(summary = "Excluir usuário por ID", description = "Exclui um Usuário específico.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar usuário por Email", description = "Retorna os detalhes de um Usuário específico.")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> findByEmail(@PathVariable String email) {
        return findUserByEmailUseCase.execute(email)
                .map(user -> ResponseEntity.ok(UserResponseDTO.fromDomain(user)))
                .orElse(ResponseEntity.notFound().build());
    }


}
