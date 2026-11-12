package com.gestaofinanceira.ms2transactions.domain.usecase;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;
import com.gestaofinanceira.ms2transactions.domain.model.TransactionType;
import com.gestaofinanceira.ms2transactions.domain.ports.in.CreateTransactionPort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.BalanceServicePort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.TransactionRepositoryPort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.UserValidationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

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

                boolean destUserExists = userValidationPort.validateUserExists(transaction.getDestinationUserId());
                if (!destUserExists) {
                    transaction.reject("Usuário de destino não encontrado no sistema.");
                    return transactionRepositoryPort.save(transaction);
                }

                if (transaction.getUserId().equals(transaction.getDestinationUserId())) {
                    transaction.reject("Não é permitido transferir para a mesma conta.");
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