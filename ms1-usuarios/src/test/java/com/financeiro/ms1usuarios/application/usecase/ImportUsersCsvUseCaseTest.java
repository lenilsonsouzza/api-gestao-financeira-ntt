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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    @DisplayName("Deve lançar exceção quando o cabeçalho do CSV for inválido")
    void shouldThrowExceptionWhenCsvHeaderIsInvalid() {
        // Arrange - CSV com colunas erradas
        String invalidCsvContent = "nome,telefone\nJoão,123456";
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file",
                "usuarios.csv",
                "text/csv",
                invalidCsvContent.getBytes()
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            importUsersCsvUseCase.execute(invalidFile);
        });

        assertTrue(exception.getMessage().contains("cabeçalho do CSV inválido"));
    }
}