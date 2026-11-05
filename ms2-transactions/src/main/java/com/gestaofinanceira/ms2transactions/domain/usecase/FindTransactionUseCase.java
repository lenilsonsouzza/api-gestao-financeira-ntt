package com.gestaofinanceira.ms2transactions.domain.usecase;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;
import com.gestaofinanceira.ms2transactions.domain.ports.out.TransactionRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FindTransactionUseCase {

    private final TransactionRepositoryPort transactionRepositoryPort;

    public Transaction findById(Long id) {
        return transactionRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada com o ID: " + id));
    }

    public List<Transaction> findByUserId(Long userId) {
        return transactionRepositoryPort.findByUserId(userId);
    }


}
