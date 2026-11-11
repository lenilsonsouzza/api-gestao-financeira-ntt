package com.gestaofinanceira.ms2transactions.domain.usecase;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;
import com.gestaofinanceira.ms2transactions.domain.model.TransactionStatus;
import com.gestaofinanceira.ms2transactions.domain.model.TransactionType;
import com.gestaofinanceira.ms2transactions.domain.ports.out.BalanceServicePort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.TransactionRepositoryPort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.UserValidationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTransactionUseCaseTest {

    @Mock
    private TransactionRepositoryPort transactionRepositoryPort;

    @Mock
    private BalanceServicePort balanceServicePort;

    @Mock
    private UserValidationPort userValidationPort;

    @InjectMocks
    private CreateTransactionUseCase createTransactionUseCase;

    @BeforeEach
    void setUp() {
    }

    @Test
    void deveCriarDepósitoComSucesso() {
        // Arrange
        Transaction transaction = new Transaction(1L, TransactionType.DEPOSIT, new BigDecimal("500.00"), "Salário", "Depósito", null);

        when(userValidationPort.validateUserExists(1L)).thenReturn(true);
        when(transactionRepositoryPort.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Transaction result = createTransactionUseCase.create(transaction);

        // Assert
        assertNotNull(result);
        assertEquals(TransactionStatus.APPROVED, result.getStatus());
        verify(balanceServicePort, times(1)).credit(1L, new BigDecimal("500.00"));
        verify(transactionRepositoryPort, times(1)).save(transaction);
    }

    @Test
    void deveRejeitarTransacaoQuandoUsuarioNaoExiste() {
        // Arrange
        Transaction transaction = new Transaction(99L, TransactionType.WITHDRAWAL, new BigDecimal("100.00"), "Saque", "Saque", null);

        when(userValidationPort.validateUserExists(99L)).thenReturn(false);
        when(transactionRepositoryPort.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Transaction result = createTransactionUseCase.create(transaction);

        // Assert
        assertNotNull(result);
        assertEquals(TransactionStatus.REJECTED, result.getStatus());
        assertEquals("Usuário remetente não encontrado ou inativo no sistema.", result.getRejectionReason());
        verify(transactionRepositoryPort, times(1)).save(transaction);
    }

    @Test
    void deveRejeitarTransacaoQuandoLimiteExcedido() {
        // Arrange
        Transaction transaction = new Transaction(1L, TransactionType.WITHDRAWAL, new BigDecimal("600.00"), "Saque alto", "Saque", null);

        when(userValidationPort.validateUserExists(1L)).thenReturn(true);
        when(transactionRepositoryPort.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Transaction result = createTransactionUseCase.create(transaction);

        // Assert
        assertNotNull(result);
        assertEquals(TransactionStatus.REJECTED, result.getStatus());
        assertEquals("Transação rejeitada: Limite por transação excedido.", result.getRejectionReason());
        verify(transactionRepositoryPort, times(1)).save(transaction);
    }

    @Test
    void deveCriarOutroDepositoComSucesso() {
        // Arrange
        Transaction transaction = new Transaction(1L, TransactionType.DEPOSIT, new BigDecimal("200.00"), "Freelance", "Depósito", null);

        when(userValidationPort.validateUserExists(1L)).thenReturn(true);
        when(transactionRepositoryPort.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Transaction result = createTransactionUseCase.create(transaction);

        // Assert
        assertNotNull(result);
        assertEquals(TransactionStatus.APPROVED, result.getStatus());
        verify(balanceServicePort, times(1)).credit(1L, new BigDecimal("200.00"));
        verify(transactionRepositoryPort, times(1)).save(transaction);
    }
}