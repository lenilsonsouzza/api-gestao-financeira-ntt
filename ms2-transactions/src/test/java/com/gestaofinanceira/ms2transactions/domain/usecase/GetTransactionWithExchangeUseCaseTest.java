package com.gestaofinanceira.ms2transactions.domain.usecase;

import com.gestaofinanceira.ms2transactions.domain.model.Cotacao;
import com.gestaofinanceira.ms2transactions.domain.model.Transaction;
import com.gestaofinanceira.ms2transactions.domain.model.TransactionType;
import com.gestaofinanceira.ms2transactions.domain.ports.out.ExchangeRateServicePort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.TransactionRepositoryPort;
import com.gestaofinanceira.ms2transactions.infrastructure.web.dto.TransactionResponseWithExchange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTransactionWithExchangeUseCaseTest {

    @Mock
    private TransactionRepositoryPort transactionRepositoryPort;

    @Mock
    private ExchangeRateServicePort exchangeRateServicePort;

    @InjectMocks
    private GetTransactionWithExchangeUseCase useCase;

    @Test
    void deveRetornarTransacaoComConversaoQuandoAprovada() {
        // Arrange
        Long transactionId = 1L;
        Long userId = 1L;

        Transaction transaction = new Transaction(
                userId,
                TransactionType.PURCHASE,
                new BigDecimal("100.00"),
                "Compra",
                "Alimentação",
                null
        );
        transaction.setId(transactionId);
        transaction.approve();

        when(transactionRepositoryPort.findById(transactionId)).thenReturn(Optional.of(transaction));

        when(exchangeRateServicePort.buscar("USD")).thenReturn(Optional.of(new Cotacao("USD", new BigDecimal("5.00"), LocalDate.now())));
        when(exchangeRateServicePort.buscar("EUR")).thenReturn(Optional.of(new Cotacao("EUR", new BigDecimal("6.00"), LocalDate.now())));

        // Act
        TransactionResponseWithExchange response = useCase.execute(transactionId, userId);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.conversoes().size());
        assertNull(response.message());
        verify(transactionRepositoryPort, times(1)).findById(transactionId);
    }

    @Test
    void deveLancarExcecaoQuandoTransacaoNaoPertenceAoUsuario() {
        // Arrange
        Long transactionId = 1L;
        Long userId = 1L;
        Long outroUsuarioId = 2L;

        Transaction transaction = new Transaction(
                outroUsuarioId,
                TransactionType.PURCHASE,
                new BigDecimal("100.00"),
                "Compra",
                "Alimentação",
                null
        );
        transaction.setId(transactionId);

        when(transactionRepositoryPort.findById(transactionId)).thenReturn(Optional.of(transaction));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            useCase.execute(transactionId, userId);
        });

        assertTrue(exception.getMessage().contains("Acesso negado"));
        verify(transactionRepositoryPort, times(1)).findById(transactionId);
        verifyNoInteractions(exchangeRateServicePort);
    }
}