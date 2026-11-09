package com.gestaofinanceira.ms2transactions.infrastructure.web.controller;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;
import com.gestaofinanceira.ms2transactions.domain.ports.in.CreateTransactionPort;
import com.gestaofinanceira.ms2transactions.domain.usecase.ExpenseAnalysisUseCase;
import com.gestaofinanceira.ms2transactions.domain.usecase.GetBalanceUseCase;
import com.gestaofinanceira.ms2transactions.domain.usecase.GetTransactionWithExchangeUseCase;
import com.gestaofinanceira.ms2transactions.infrastructure.client.dto.BalanceResponseDto;
import com.gestaofinanceira.ms2transactions.infrastructure.web.exporter.CsvExportService;
import com.gestaofinanceira.ms2transactions.infrastructure.web.dto.ExpenseAnalysisResponseDto;
import com.gestaofinanceira.ms2transactions.infrastructure.web.dto.TransactionRequestDto;
import com.gestaofinanceira.ms2transactions.infrastructure.web.dto.TransactionResponseWithExchange;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final CreateTransactionPort createTransactionPort;
    private final GetTransactionWithExchangeUseCase getTransactionWithExchangeUseCase;
    private final ExpenseAnalysisUseCase expenseAnalysisUseCase;
    private final CsvExportService csvExportService;
    private final GetBalanceUseCase getBalanceUseCase;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody @Valid TransactionRequestDto requestDTO){
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

    @GetMapping("/analysis/{userId}")
    public ResponseEntity<ExpenseAnalysisResponseDto> getExpenseAnalysis(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        var result = expenseAnalysisUseCase.execute(userId, startDate, endDate);

        var response = new ExpenseAnalysisResponseDto(
                result.startDate().toString(),
                result.endDate().toString(),
                result.totalGeneral(),
                result.expensesByCategory(),
                result.expensesByDay(),
                result.expensesByMonth()
        );

        return ResponseEntity.ok(response);
    }
    @GetMapping(value = "/analysis/{userId}/csv", produces = "text/csv")
    public ResponseEntity<String> exportExpenseAnalysisCsv(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        var result = expenseAnalysisUseCase.execute(userId, startDate, endDate);
        String csvContent = csvExportService.exportExpenseAnalysisToCsv(result);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-despesas.csv")
                .body(csvContent);
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<BalanceResponseDto> getBalance(@PathVariable Long userId) {
      BalanceResponseDto response = getBalanceUseCase.execute(userId);
        return ResponseEntity.ok(response);
    }

}