package com.financeiro.ms1usuarios.application.usecase;

import com.financeiro.ms1usuarios.application.ports.out.UserRepositoryPort;
import com.financeiro.ms1usuarios.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateUserUseCaseTest {
    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private UpdateUserUseCase updateUserUseCase;

    @Test
    @DisplayName("Deve atualizar o usuário com sucesso quando o ID existir")
    public void shouldUpdateUserSuccessfullyWhenIdExists() {
        // Arrange
        Long userId = 1L;
        User existingUser = new User(userId, "Jose", "jose@email.com", "senha123");
        User updatedData = new User(userId, "Jose Lenilson Updated", "jose.novo@email.com", "senha123");

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepositoryPort.save(any(User.class))).thenReturn(updatedData);

        // Act
        User result = updateUserUseCase.execute(userId, updatedData);

        // Assert
        assertNotNull(result);
        assertEquals("Jose Lenilson Updated", result.getName());
        assertEquals("jose.novo@email.com", result.getEmail());
        verify(userRepositoryPort, times(1)).findById(userId);
        verify(userRepositoryPort, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar usuário que não existe")
    public void shouldThrowExceptionWhenUpdatingNonExistentUser() {
        // Arrange
        Long userId = 99L;
        User updatedData = new User(userId, "Nome", "email@email.com", "senha");

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            updateUserUseCase.execute(userId, updatedData);
        });

        assertEquals("Usuario não encontrado com o ID: 99", exception.getMessage());
        verify(userRepositoryPort, times(1)).findById(userId);
        verify(userRepositoryPort, never()).save(any(User.class));
    }

}
