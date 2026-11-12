package com.gestaofinanceira.ms2transactions.domain.usecase;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;
import com.gestaofinanceira.ms2transactions.domain.model.TransactionType;
import com.gestaofinanceira.ms2transactions.domain.ports.in.CreateTransactionPort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.BalanceServicePort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.TransactionRepositoryPort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.UserValidationPort;
import com.gestaofinanceira.ms2transactions.infrastructure.client.dto.BalanceResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
public class CreateTransactionUseCase implements CreateTransactionPort {

    private final TransactionRepositoryPort transactionRepositoryPort;
    private final BalanceServicePort balanceServicePort;
    private final UserValidationPort userValidationPort;

    @Override
    public Transaction create(Transaction transaction) {
        try {
            boolean userExists = userValidationPort.validateUserExists(transaction.getUserId());
            if (!userExists) {
                transaction.reject("Usuário remetente não encontrado ou inativo no sistema.");
                return transactionRepositoryPort.save(transaction);
            }

            if (transaction.getType() == TransactionType.TRANSFER) {
                if (transaction.getDestinationUserId() == null) {
                    transaction.reject("Usuário de destino é obrigatório para transferências.");
                    return transactionRepositoryPort.save(transaction);
                }

                if (transaction.getUserId().equals(transaction.getDestinationUserId())) {
                    transaction.reject("Não é permitido transferir para a mesma conta.");
                    return transactionRepositoryPort.save(transaction);
                }

                try {
                    boolean destUserExists = userValidationPort.validateUserExists(transaction.getDestinationUserId());
                    if (!destUserExists) {
                        transaction.reject("Usuário de destino não encontrado no sistema.");
                        return transactionRepositoryPort.save(transaction);
                    }
                } catch (Exception e) {
                    log.error("Erro ao comunicar com MS1 para validar usuário de destino [{}]: {}", transaction.getDestinationUserId(), e.getMessage());
                    transaction.reject("Erro ao validar usuário de destino no serviço de utilizadores.");
                    return transactionRepositoryPort.save(transaction);
                }
            }

            if (transaction.getType() == TransactionType.DEPOSIT) {
                balanceServicePort.credit(transaction.getUserId(), transaction.getAmount());
                transaction.approve();
                return transactionRepositoryPort.save(transaction);
            }

            if (transaction.getType() == TransactionType.PURCHASE ||
                    transaction.getType() == TransactionType.WITHDRAWAL ||
                    transaction.getType() == TransactionType.TRANSFER) {

                boolean hasLimit = balanceServicePort.validateLimit(transaction.getUserId(), transaction.getAmount());
                if (!hasLimit) {
                    transaction.reject("Transação rejeitada: Limite por transação excedido.");
                    return transactionRepositoryPort.save(transaction);
                }

                try {
                    BalanceResponseDto balanceData = balanceServicePort.getBalanceData(transaction.getUserId());
                    if (balanceData != null && balanceData.limiteDiario() != null) {
                        BigDecimal spentToday = transactionRepositoryPort.getSpentToday(transaction.getUserId(), LocalDate.now());
                        if (spentToday == null) {
                            spentToday = BigDecimal.ZERO;
                        }

                        if (spentToday.add(transaction.getAmount()).compareTo(balanceData.limiteDiario()) > 0) {
                            transaction.reject("Transação rejeitada: Limite diário excedido.");
                            return transactionRepositoryPort.save(transaction);
                        }
                    }
                } catch (Exception e) {
                    log.warn("Não foi possível validar o limite diário no serviço externo: {}", e.getMessage());
                }

                BigDecimal currentBalance = balanceServicePort.getBalance(transaction.getUserId());
                if (currentBalance.compareTo(transaction.getAmount()) < 0) {
                    transaction.reject("Transação rejeitada: Saldo insuficiente.");
                    return transactionRepositoryPort.save(transaction);
                }

                balanceServicePort.debit(transaction.getUserId(), transaction.getAmount());

                if (transaction.getType() == TransactionType.TRANSFER) {
                    try {
                        balanceServicePort.credit(transaction.getDestinationUserId(), transaction.getAmount());
                    } catch (Exception e) {
                        log.error("Falha ao creditar na conta de destino [{}]. Estornando valor para a origem [{}]. Erro: {}",
                                transaction.getDestinationUserId(), transaction.getUserId(), e.getMessage(), e);

                        balanceServicePort.credit(transaction.getUserId(), transaction.getAmount());

                        transaction.reject("Transação rejeitada: falha no processamento da conta de destino, valor estornado.");
                        return transactionRepositoryPort.save(transaction);
                    }
                }
            }

            transaction.approve();

        } catch (RuntimeException e) {
            log.error("Erro crítico ao processar transação para o usuário [{}]: {}", transaction.getUserId(), e.getMessage(), e);
            transaction.reject("Serviço externo indisponível no momento.");
        }

        return transactionRepositoryPort.save(transaction);
    }
}