package com.financeiro.ms1usuarios.application.usecase;

import com.financeiro.ms1usuarios.application.ports.out.UserRepositoryPort;
import com.financeiro.ms1usuarios.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindAllUsersUseCaseTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private FindAllUsersUseCase findAllUsersUseCase;

    @Test
    @DisplayName("Retorar uma lista com todos os usuarios com sucesso")
    public void shouldReturnAllUserSucessfully() {
        //Arrange
        List<User> mockUsers = Arrays.asList(
                new User(1L, "Jose lenilson", "jose@email.com", "senha1234"),
                new User(2L, "Maria silva", "maria@email.com", "senha1234")
        );
        when(userRepositoryPort.findAll()).thenReturn(mockUsers);

        //Act
        List<User> result = findAllUsersUseCase.execute();

        //Assert
        assertNotNull(result);
        assertEquals(2,result.size());
        assertEquals("Jose lenilson", result.get(0).getName());
        verify(userRepositoryPort,times(1)).findAll();
    }
    @Test
    @DisplayName("Retornar uma lista vazia quando não houver usuários cadastrados")
    public void shouldReturnEmptyListWhenNoUsersExist() {
        //Arrange
        when(userRepositoryPort.findAll()).thenReturn(Collections.emptyList());

        //Act
        List<User> result = findAllUsersUseCase.execute();

        //Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepositoryPort,times(1)).findAll();


    }
}
