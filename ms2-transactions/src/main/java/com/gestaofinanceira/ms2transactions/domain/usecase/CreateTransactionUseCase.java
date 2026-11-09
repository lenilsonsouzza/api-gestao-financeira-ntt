package com.gestaofinanceira.ms2transactions.domain.usecase;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;
import com.gestaofinanceira.ms2transactions.domain.model.TransactionType;
import com.gestaofinanceira.ms2transactions.domain.ports.in.CreateTransactionPort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.BalanceServicePort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.TransactionRepositoryPort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.UserValidationPort;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

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
            }


            if (transaction.getType() == TransactionType.DEPOSIT) {
                transaction.approve();
                return transactionRepositoryPort.save(transaction);
            }


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


            transaction.approve();

        } catch (RuntimeException e) {
            e.printStackTrace();
            transaction.reject("Serviço externo indisponível no momento: " + e.getMessage());
        }

        return transactionRepositoryPort.save(transaction);
    }
}