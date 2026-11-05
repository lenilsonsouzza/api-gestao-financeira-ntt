package com.gestaofinanceira.ms2transactions.domain.ports.out;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);

    Optional<Transaction> findById(Long id);

    List<Transaction> findByUserId(Long userId);
}
