package com.gestaofinanceira.ms2transactions.infrastructure.persistence.repository;

import com.gestaofinanceira.ms2transactions.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringTransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByUserId(Long userId);
}
