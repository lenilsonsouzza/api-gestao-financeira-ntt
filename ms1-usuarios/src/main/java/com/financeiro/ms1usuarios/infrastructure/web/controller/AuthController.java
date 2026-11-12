package com.financeiro.ms1usuarios.infrastructure.web.controller;

import com.financeiro.ms1usuarios.application.usecase.FindUserByEmailUseCase;
import com.financeiro.ms1usuarios.domain.exception.InvalidCredentialsException;
import com.financeiro.ms1usuarios.domain.model.User;
import com.financeiro.ms1usuarios.infrastructure.security.JwtService;
import com.financeiro.ms1usuarios.infrastructure.web.dto.LoginRequestDTO;
import com.financeiro.ms1usuarios.infrastructure.web.dto.LoginResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação de utilizadores e emissão de tokens JWT")
@RequiredArgsConstructor
public class AuthController {

    private final FindUserByEmailUseCase findUserByEmailUseCase;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "Realizar login no sistema", description = "Valida as credenciais do utilizador (e-mail e senha) e retorna um token JWT válido para acesso aos endpoints protegidos.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {

        User user = findUserByEmailUseCase.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("E-mail ou senha inválidos."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("E-mail ou senha inválidos.");
        }

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}