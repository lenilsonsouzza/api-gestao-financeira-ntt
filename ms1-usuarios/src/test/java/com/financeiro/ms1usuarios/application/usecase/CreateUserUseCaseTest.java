package com.financeiro.ms1usuarios.application.usecase;

import com.financeiro.ms1usuarios.application.ports.out.UserRepositoryPort;
import com.financeiro.ms1usuarios.domain.exception.EmailAlreadyExistsException;
import com.financeiro.ms1usuarios.domain.model.User;
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
public class CreateUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Test
    @DisplayName("Deve criar o usuário com sucesso quando os dados forem válidos")
    public void shouldCreateUserSuccessfully() {
        User userToCreate = new User(null, "jose lenilson", "jose@email.com", "senha1234");
        User savedUser = new User(1L, "jose lenilson", "jose@email.com", "encoded_senha");

        when(userRepositoryPort.findByEmail("jose@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("senha1234")).thenReturn("encoded_senha");
        when(userRepositoryPort.save(any(User.class))).thenReturn(savedUser);

        User result = createUserUseCase.create(userToCreate);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("jose lenilson", result.getName());
        assertEquals("jose@email.com", result.getEmail());

        verify(userRepositoryPort, times(1)).findByEmail("jose@email.com");
        verify(passwordEncoder, times(1)).encode("senha1234");
        verify(userRepositoryPort, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve encriptar a senha do usuário antes de salvar")
    public void shouldEncodePasswordWhenCreatingUser() {
        User userToCreate = new User(null, "jose lenilson", "jose@email.com", "senha1234");

        when(userRepositoryPort.findByEmail("jose@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("senha1234")).thenReturn("hash_seguro");
        when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        createUserUseCase.create(userToCreate);

        verify(passwordEncoder, times(1)).encode("senha1234");
    }

    @Test
    @DisplayName("Deve lançar EmailAlreadyExistsException quando o e-mail já estiver cadastrado")
    public void shouldThrowExceptionWhenEmailAlreadyExists() {
        User userToCreate = new User(null, "jose lenilson", "jose@email.com", "senha1234");
        User existingUser = new User(1L, "Outro User", "jose@email.com", "outra_senha");

        when(userRepositoryPort.findByEmail("jose@email.com")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class, () -> {
            createUserUseCase.create(userToCreate);
        });

        verify(userRepositoryPort, times(1)).findByEmail("jose@email.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepositoryPort, never()).save(any(User.class));
    }
}