package com.gestaofinanceira.ms2transactions.adapter;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;
import com.gestaofinanceira.ms2transactions.domain.ports.out.TransactionRepositoryPort;
import com.gestaofinanceira.ms2transactions.infrastructure.persistence.entity.TransactionEntity;
import com.gestaofinanceira.ms2transactions.infrastructure.persistence.mapper.TransactionMapper;
import com.gestaofinanceira.ms2transactions.infrastructure.persistence.repository.SpringTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
}
