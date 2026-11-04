package com.financeiro.ms1usuarios.infrastructure.web.controller;


import com.financeiro.ms1usuarios.application.usecase.ImportUsersCsvUseCase;
import com.financeiro.ms1usuarios.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Tag(name = "Cadastro em Massa",description = "Cadastrar usuarios em Massa por Arquivo CSV")
@RequestMapping("/users")
@RestController
public class UserCsvController {

    private final ImportUsersCsvUseCase importUsersCsvUseCase;

    @Operation(summary = "Cadastrar varios Usuários",description = "Realiza o Cadastro de varios Usuários atraves de um arquivo CSV")
    @PostMapping("/import-csv")
    public ResponseEntity<List<User>> importCsv(@RequestParam("file")MultipartFile file) {
        List<User> users = importUsersCsvUseCase.execute(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

}
