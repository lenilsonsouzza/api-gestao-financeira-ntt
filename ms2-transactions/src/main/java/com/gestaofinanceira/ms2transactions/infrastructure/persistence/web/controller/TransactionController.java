package com.gestaofinanceira.ms2transactions.infrastructure.persistence.web.controller;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;
import com.gestaofinanceira.ms2transactions.domain.ports.in.CreateTransactionPort;
import com.gestaofinanceira.ms2transactions.domain.usecase.GetTransactionWithExchangeUseCase;
import com.gestaofinanceira.ms2transactions.domain.usecase.TransactionResponseWithExchange;
import com.gestaofinanceira.ms2transactions.infrastructure.persistence.web.dto.TransactionRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final CreateTransactionPort createTransactionPort;
    private final GetTransactionWithExchangeUseCase getTransactionWithExchangeUseCase;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionRequestDTO requestDTO){
        Transaction transaction = new Transaction(
                requestDTO.userId(),
                requestDTO.type(),
                requestDTO.amount(),
                requestDTO.description(),
                requestDTO.category(),
                requestDTO.destinationUserId()
        );

        Transaction savedTransaction = createTransactionPort.create(transaction);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
    }

    @GetMapping("/{id}/exchange")
    public ResponseEntity<TransactionResponseWithExchange> getTransactionWithExchange(
            @PathVariable Long id,
            @RequestParam(defaultValue = "USD") String currency) {

        TransactionResponseWithExchange response = getTransactionWithExchangeUseCase.execute(id, currency);
        return ResponseEntity.ok(response);
    }
}
