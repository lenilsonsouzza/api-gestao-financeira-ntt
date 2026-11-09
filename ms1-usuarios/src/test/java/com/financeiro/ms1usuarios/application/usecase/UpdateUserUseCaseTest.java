package com.financeiro.ms1usuarios.application.usecase;

import com.financeiro.ms1usuarios.application.ports.out.UserRepositoryPort;
import com.financeiro.ms1usuarios.domain.exception.UserNotFoundException;
import com.financeiro.ms1usuarios.domain.model.User;
import com.sun.jdi.request.DuplicateRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

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
        when(userRepositoryPort.findByEmail("jose.novo@email.com")).thenReturn(Optional.empty());
        when(userRepositoryPort.save(any(User.class))).thenReturn(updatedData);

        // Act
        User result = updateUserUseCase.update(userId, updatedData);

        // Assert
        assertNotNull(result);
        assertEquals("Jose Lenilson Updated", result.getName());
        assertEquals("jose.novo@email.com", result.getEmail());
        verify(userRepositoryPort, times(1)).findById(userId);
        verify(userRepositoryPort, times(1)).findByEmail("jose.novo@email.com");
        verify(userRepositoryPort, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar com e-mail pertencente a outro usuário")
    public void shouldThrowExceptionWhenUpdatingWithDuplicateEmailFromAnotherUser() {
        // Arrange
        Long userId = 1L;
        Long anotherUserId = 2L;

        User existingUser = new User(userId, "Jose", "jose@email.com", "senha123");
        User anotherUserWithSameEmail = new User(anotherUserId, "Outro Usuário", "outro@email.com", "senha123");
        User updatedData = new User(userId, "Jose", "outro@email.com", "senha123");

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepositoryPort.findByEmail("outro@email.com")).thenReturn(Optional.of(anotherUserWithSameEmail));

        // Act & Assert
        DuplicateRequestException exception = assertThrows(DuplicateRequestException.class, () -> {
            updateUserUseCase.update(userId, updatedData);
        });

        assertEquals("O e-mail informado já está em uso por outro Usuário.", exception.getMessage());
        verify(userRepositoryPort, times(1)).findById(userId);
        verify(userRepositoryPort, times(1)).findByEmail("outro@email.com");
        verify(userRepositoryPort, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve permitir atualizar usuário mantendo o mesmo e-mail")
    public void shouldAllowUpdateWhenEmailBelongsToSameUser() {
        // Arrange
        Long userId = 1L;
        User existingUser = new User(userId, "Jose", "jose@email.com", "senha123");
        User updatedData = new User(userId, "Jose Silva", "jose@email.com", "senha123");

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepositoryPort.findByEmail("jose@email.com")).thenReturn(Optional.of(existingUser));
        when(userRepositoryPort.save(any(User.class))).thenReturn(updatedData);

        // Act
        User result = updateUserUseCase.update(userId, updatedData);

        // Assert
        assertNotNull(result);
        verify(userRepositoryPort, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException ao tentar atualizar usuário que não existe")
    public void shouldThrowUserNotFoundExceptionWhenUpdatingNonExistentUser() {
        // Arrange
        Long userId = 99L;
        User updatedData = new User(userId, "Nome", "email@email.com", "senha");

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            updateUserUseCase.update(userId, updatedData);
        });

        // Corrigido com acento no "Usuário"
        assertEquals("Usuário não encontrado com o ID: 99", exception.getMessage());
        verify(userRepositoryPort, times(1)).findById(userId);
        verify(userRepositoryPort, never()).save(any(User.class));
    }
}