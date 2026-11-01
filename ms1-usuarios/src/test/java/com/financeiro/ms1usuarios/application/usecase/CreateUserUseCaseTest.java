package com.financeiro.ms1usuarios.application.usecase;

import com.financeiro.ms1usuarios.application.ports.out.UserRepositoryPort;
import com.financeiro.ms1usuarios.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Test
    @DisplayName("TESTE CRIADO COM  SUCESSO")
    public void shouldCreateUserSucessfully(){
     User userToCreate = new User(null,"jose lenilson","jose@email.com","senha1234");
     User saveUser = new User(1L, "jose lenilson","jose@email.com","senha1234");

     when(userRepositoryPort.save(any(User.class))).thenReturn(saveUser);

     User result = createUserUseCase.execute(userToCreate);
     assertNotNull(result);
     assertEquals(1l,result.getId());
     assertEquals("jose lenilson",result.getName());
     assertEquals("jose@email.com",result.getEmail());

     verify(userRepositoryPort, times(1)).save(any(User.class));


    }

}
