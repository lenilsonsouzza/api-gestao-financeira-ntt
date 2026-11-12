package com.gestaofinanceira.ms2transactions.domain.ports.in;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;

public interface CreateTransactionPort {
    Transaction create(Transaction transaction);
}
