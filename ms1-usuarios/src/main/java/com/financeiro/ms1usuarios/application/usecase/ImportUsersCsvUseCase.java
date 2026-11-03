package com.financeiro.ms1usuarios.application.usecase;

import com.financeiro.ms1usuarios.application.ports.out.UserRepositoryPort;
import com.financeiro.ms1usuarios.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ImportUsersCsvUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public List<User> execute(MultipartFile file) {
        List<User> importedUsers = new ArrayList<>();
        if (file.isEmpty()) {
            throw new RuntimeException("O arquivo CSV enviado está vazio. ");

        }
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String name = data[0].trim();
                    String email = data[1].trim();
                    String password = data[2].trim();

                    User user = new User(null, name, email, password);
                    User savedUser = userRepositoryPort.save(user);
                    importedUsers.add(savedUser);

                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar o arquivo CSV: " + e.getMessage());
        }

        return importedUsers;

    }


}
