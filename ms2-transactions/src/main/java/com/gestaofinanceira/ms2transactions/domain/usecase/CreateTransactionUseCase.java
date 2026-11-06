package com.gestaofinanceira.ms2transactions.domain.usecase;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;
import com.gestaofinanceira.ms2transactions.domain.model.TransactionType;
import com.gestaofinanceira.ms2transactions.domain.ports.in.CreateTransactionPort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.BalanceServicePort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.TransactionRepositoryPort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.UserValidationPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateTransactionUseCase implements CreateTransactionPort {

    private final TransactionRepositoryPort transactionRepositoryPort;
    private final BalanceServicePort balanceServicePort;
    private  final UserValidationPort userValidationPort;

    @Override
    public Transaction create(Transaction transaction) {

        boolean userExists = userValidationPort.validateUserExists(transaction.getUserId());
        if (!userExists) {
            transaction.reject("Utilizador remetente não encontrado ou inativo no sistema.");
            return transactionRepositoryPort.save(transaction);
        }


        if (transaction.getType() == TransactionType.TRANSFER && transaction.getDestinationUserId() == null) {
            transaction.reject("Usuário de destino é obrigatório para transferências.");
            return transactionRepositoryPort.save(transaction);
        }

        if (transaction.getType() == TransactionType.TRANSFER && transaction.getDestinationUserId() != null) {
            boolean destUserExists = userValidationPort.validateUserExists(transaction.getDestinationUserId());
            if (!destUserExists) {
                transaction.reject("Utilizador de destino não encontrado no sistema.");
                return transactionRepositoryPort.save(transaction);
            }
        }


        if (transaction.getType() == TransactionType.DEPOSIT) {
            transaction.approve();
            return transactionRepositoryPort.save(transaction);
        }


        boolean hasLimit = balanceServicePort.validateLimit(transaction.getUserId(), transaction.getAmount());

        if (hasLimit) {
            transaction.approve();
        } else {
            transaction.reject("Saldo ou Limite insuficiente");
        }

        return transactionRepositoryPort.save(transaction);
    }
}