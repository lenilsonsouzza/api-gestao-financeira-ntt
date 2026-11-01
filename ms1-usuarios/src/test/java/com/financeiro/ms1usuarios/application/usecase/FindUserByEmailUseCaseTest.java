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
public class FindUserByEmailUseCaseTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private FindUserByEmailUseCase findUserByEmailUseCase;

    @Test
    @DisplayName("Retornar Usuario quando o email Existir")
    public void shouldReturnUserOptionalWhenEmailExists() {

        //Arrage
        String email = "jose@email.com";
        User mockUser = new User(1L, "Jose Lenilson", "jose@email.com", "senha1234");
        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(mockUser));

        //Act
        Optional<User> result = findUserByEmailUseCase.execute(email);

        //Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }
    @Test
    @DisplayName("retornar vazio quando o e-mail não for encontrado")
    public void shouldReturnEmptyOptionalWhenEmailNotFound() {
        // Arrange
        String email = "naoexiste@email.com";
        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = findUserByEmailUseCase.execute(email);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepositoryPort, times(1)).findByEmail(email);
    }


    }
