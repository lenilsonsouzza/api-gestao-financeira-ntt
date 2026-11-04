package com.financeiro.ms1usuarios.application.usecase;

import com.financeiro.ms1usuarios.application.ports.in.CreateUserInboundPort;
import com.financeiro.ms1usuarios.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ImportUsersCsvUseCaseTest {

    @Mock
    private CreateUserInboundPort createUserInboundPort;

    @InjectMocks
    private ImportUsersCsvUseCase importUsersCsvUseCase; // Substitui pelo nome exato da tua classe de importação

    @Test
    @DisplayName("Deve processar o ficheiro CSV e chamar o caso de uso de criação para cada linha")
    void shouldProcessCsvAndCreateUsersSuccessfully() {
        // Arrange
        String csvContent = "name,email,password\nJoão Silva,joao@email.com,123456\nMaria Souza,maria@email.com,654321";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "usuarios.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // Act
        importUsersCsvUseCase.execute(file); // Ajusta o nome do método ("execute" ou "importCsv") conforme o teu código

        // Assert
        verify(createUserInboundPort, times(2)).create(any(User.class));
    }
}