package com.gestaofinanceira.ms2transactions.infrastructure.adapter;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;
import com.gestaofinanceira.ms2transactions.domain.model.TransactionType;
import com.gestaofinanceira.ms2transactions.domain.ports.out.TransactionRepositoryPort;
import com.gestaofinanceira.ms2transactions.infrastructure.persistence.entity.TransactionEntity;
import com.gestaofinanceira.ms2transactions.infrastructure.persistence.mapper.TransactionMapper;
import com.gestaofinanceira.ms2transactions.infrastructure.persistence.repository.SpringTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final SpringTransactionRepository springTransactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity =transactionMapper.toEntity(transaction);
        TransactionEntity savedEntity = springTransactionRepository.save(entity);
        return transactionMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return springTransactionRepository.findById(id)
                .map(transactionMapper::toDomain);
    }

    @Override
    public List<Transaction> findByUserId(Long userId) {
        return springTransactionRepository.findByUserId(userId).stream()
                .map(transactionMapper::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> findByUserIdAndPeriod(Long userId, LocalDate starDate, LocalDate endDate) {
        LocalDateTime startDateTime = starDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        return springTransactionRepository.findByUserIdAndTimestampBetween(userId, startDateTime, endDateTime)
                .stream()
                .map(transactionMapper::toDomain)
                .toList();
    }
    @Override
    public BigDecimal getSpentToday(Long userId, LocalDate date) {
        List<Transaction> transactionsToday = findByUserIdAndPeriod(userId, date, date);

        return transactionsToday.stream()
                .filter(t -> t.getStatus() != null && t.getStatus().name().equals("APPROVED"))
                .filter(t -> t.getType() == TransactionType.PURCHASE ||
                        t.getType() == TransactionType.WITHDRAWAL ||
                        t.getType() == TransactionType.TRANSFER)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
