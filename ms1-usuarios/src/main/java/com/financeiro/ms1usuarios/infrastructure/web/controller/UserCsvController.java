package com.financeiro.ms1usuarios.infrastructure.web.controller;


import com.financeiro.ms1usuarios.application.ports.in.CreateUserInboundPort;
import com.financeiro.ms1usuarios.application.ports.in.ImportUsersCsvInboundPort;
import com.financeiro.ms1usuarios.application.usecase.ImportUsersCsvUseCase;
import com.financeiro.ms1usuarios.domain.model.User;
import com.financeiro.ms1usuarios.infrastructure.web.dto.CsvImportReportResponse;
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

    private  final ImportUsersCsvInboundPort importUsersCsvInboundPort;

    @Operation(summary = "Cadastrar varios Usuários",description = "Realiza o Cadastro de varios Usuários atraves de um arquivo CSV")
    @PostMapping("/import-csv")
    public ResponseEntity<CsvImportReportResponse> importCsv(@RequestParam("file")MultipartFile file) {
        CsvImportReportResponse report = importUsersCsvInboundPort.execute(file);
        return ResponseEntity.status(HttpStatus.OK).body(report);
    }

}
