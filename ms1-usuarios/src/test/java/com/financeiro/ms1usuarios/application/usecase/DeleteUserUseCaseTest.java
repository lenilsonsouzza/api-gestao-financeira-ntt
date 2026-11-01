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
public class DeleteUserUseCaseTest {

    @Mock
    UserRepositoryPort userRepositoryPort;
    @InjectMocks
    DeleteUserUseCase deleteUserUseCase;

    @Test
    @DisplayName("Deletar usuario com sucesso quando o Id Existir")
    public void shouldDeleteUserSuccessfullyWhenIdExists() {

        //Arrage
        Long userId = 1L;
        User existingUser = new User(userId, "Jose lenilson", "jose@email.com", "senha1234");
        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(existingUser));
        doNothing().when(userRepositoryPort).deletaById(userId);

        // Act & Asset
        assertDoesNotThrow(() -> {
            deleteUserUseCase.execute(userId);
        });
    }

    @Test
    @DisplayName("lançar exceção ao tentar deletar usuaro não existente")
    public void shouldThrowExceptionWhenDeletingNonExistentUser() {

        //Arrange
        Long userId = 99L;
        when(userRepositoryPort.findById(userId)).thenReturn(Optional.empty());

        //Act & Assert

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deleteUserUseCase.execute(userId);
        });

        assertEquals("Usuario não encontrado com o ID 99", exception.getLocalizedMessage());
        verify(userRepositoryPort, times(1)).findById(userId);
        verify(userRepositoryPort, never()).deletaById(anyLong());
    }


}






