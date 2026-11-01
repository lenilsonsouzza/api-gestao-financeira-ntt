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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindUserByIdUseCaseTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;
    @InjectMocks
    private FindUserByIdUseCase findUserByIdUseCase;

    @Test
    @DisplayName("Deve retornar um usuário com sucesso quando o ID existir")
    public void shouldReturnUserWhenIdExists() {

        // Arrange
        Long userId = 1L;
        User mockUser = new User(1L, "Jose lenilson", "jose@email.com", "senha1234");
        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        Optional<User> result = findUserByIdUseCase.execute(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.get().getId());
        assertTrue(result.isPresent());
        assertEquals("Jose lenilson", result.get().getName());
        verify(userRepositoryPort, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário não for encontrado pelo ID")
    public void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        Long userId = 99L;
        when(userRepositoryPort.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = findUserByIdUseCase.execute(userId);

        //Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepositoryPort, times(1)).findById(userId);
    }
}
