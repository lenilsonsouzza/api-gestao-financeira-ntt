package com.gestaofinanceira.ms2transactions.domain.ports.out;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);

    Optional<Transaction> findById(Long id);

    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByUserIdAndPeriod(Long userId, LocalDate starDate, LocalDate endDate);

    BigDecimal getSpentToday(Long userId, LocalDate date);
}
