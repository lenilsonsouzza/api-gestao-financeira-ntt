package com.financeiro.ms1usuarios.application.usecase;

import com.financeiro.ms1usuarios.application.ports.in.CreateUserInboundPort;
import com.financeiro.ms1usuarios.application.ports.in.ImportUsersCsvInboundPort;
import com.financeiro.ms1usuarios.domain.model.User;
import com.financeiro.ms1usuarios.infrastructure.web.dto.CsvErrorDetail;
import com.financeiro.ms1usuarios.infrastructure.web.dto.CsvImportReportResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ImportUsersCsvUseCase implements ImportUsersCsvInboundPort {

    private final CreateUserInboundPort createUserInboundPort;

    private static final List<String> EXPECTED_HEADERS = Arrays.asList("name", "email", "password");

    @Override
    public CsvImportReportResponse execute(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo CSV enviado está vazio");
        }

        List<String> sucessoEmails = new ArrayList<>();
        List<CsvErrorDetail> errors = new ArrayList<>();
        int totalProcessed = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            CSVFormat csvFormat = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim();
            CSVParser csvParser = csvFormat.parse(reader);

            List<String> fileHeaders = csvParser.getHeaderNames();
            if (!fileHeaders.stream().map(String::toLowerCase).toList().containsAll(EXPECTED_HEADERS)) {
                throw new IllegalArgumentException("cabeçalho do CSV inválido. O arquivo deve conter exatamente as colunas: name, email, password");
            }

            for (CSVRecord record : csvParser) {
                totalProcessed++;
                int lineNumber = (int) record.getRecordNumber() + 1;
                try {
                    String name = record.get("name");
                    String email = record.get("email");
                    String password = record.get("password");
                    if (name == null || name.isBlank() || email == null || email.isBlank() || password == null || password.isBlank()) {
                        throw new IllegalArgumentException("Campos obrigatórios em falta na linha");
                    }
                    User newUser = new User(null, name, email, password);

                    createUserInboundPort.create(newUser);

                    sucessoEmails.add(email);

                } catch (Exception e) {
                    errors.add(new CsvErrorDetail(lineNumber, e.getMessage()));
                }
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar o arquivo CSV: " + e.getMessage());
        }
        return new CsvImportReportResponse(totalProcessed, sucessoEmails.size(), errors.size(), errors);
    }


}



