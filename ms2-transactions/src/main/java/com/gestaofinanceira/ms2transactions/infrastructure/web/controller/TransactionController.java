package com.gestaofinanceira.ms2transactions.infrastructure.web.controller;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;
import com.gestaofinanceira.ms2transactions.domain.model.TransactionStatus;
import com.gestaofinanceira.ms2transactions.domain.model.TransactionType;
import com.gestaofinanceira.ms2transactions.domain.ports.in.CreateTransactionPort;
import com.gestaofinanceira.ms2transactions.domain.ports.out.TransactionRepositoryPort;
import com.gestaofinanceira.ms2transactions.domain.usecase.ExpenseAnalysisUseCase;
import com.gestaofinanceira.ms2transactions.domain.usecase.GetBalanceUseCase;
import com.gestaofinanceira.ms2transactions.domain.usecase.GetTransactionWithExchangeUseCase;
import com.gestaofinanceira.ms2transactions.infrastructure.client.dto.BalanceResponseDto;
import com.gestaofinanceira.ms2transactions.infrastructure.web.exporter.CsvExportService;
import com.gestaofinanceira.ms2transactions.infrastructure.web.dto.ExpenseAnalysisResponseDto;
import com.gestaofinanceira.ms2transactions.infrastructure.web.dto.TransactionRequestDto;
import com.gestaofinanceira.ms2transactions.infrastructure.web.dto.TransactionResponseWithExchange;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
@Tag(name = "Transações", description = "Endpoints para gestão, análise, câmbio e relatórios de transações financeiras")
public class TransactionController {
    private final CreateTransactionPort createTransactionPort;
    private final GetTransactionWithExchangeUseCase getTransactionWithExchangeUseCase;
    private final ExpenseAnalysisUseCase expenseAnalysisUseCase;
    private final CsvExportService csvExportService;
    private final GetBalanceUseCase getBalanceUseCase;
    private final TransactionRepositoryPort transactionRepositoryPort;

    private Long getAuthenticatedUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Operation(summary = "Criar uma nova transação", description = "Regista uma nova transação financeira (depósito, levantamento, compra ou transferência) para o utilizador autenticado.")
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionRequestDto requestDTO) {

        Long authenticatedUserId = getAuthenticatedUserId();

        Transaction transaction = new Transaction(
                authenticatedUserId,
                requestDTO.type(),
                requestDTO.amount(),
                requestDTO.description(),
                requestDTO.category(),
                requestDTO.destinationUserId()
        );

        Transaction savedTransaction = createTransactionPort.create(transaction);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
    }

    @Operation(summary = "Buscar transação com cotação de câmbio", description = "Retorna os detalhes de uma transação específica convertida para moedas estrangeiras (USD, EUR).")
    @GetMapping("/{id}/exchange")
    public ResponseEntity<TransactionResponseWithExchange> getTransactionWithExchange(@PathVariable Long id) {
        Long userId = getAuthenticatedUserId();
        var response = getTransactionWithExchangeUseCase.execute(id, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obter análise de despesas", description = "Retorna o resumo detalhado e categorizado das despesas do utilizador num determinado período opcional.")
    @GetMapping("/analysis")
    public ResponseEntity<ExpenseAnalysisResponseDto> getExpenseAnalysis(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        Long userId = getAuthenticatedUserId();

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

    @Operation(summary = "Exportar relatório de despesas em CSV", description = "Gera e transfere um ficheiro CSV contendo o relatório detalhado das despesas aprovadas no período especificado.")
    @GetMapping(value = "/analysis/csv", produces = "text/csv;charset=UTF-8")
    public ResponseEntity<byte[]> exportAnalysisCsv(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        Long userId = getAuthenticatedUserId();

        var analysisResult = expenseAnalysisUseCase.execute(userId, startDate, endDate);

        List<Transaction> transactions = transactionRepositoryPort.findByUserIdAndPeriod(
                        userId, analysisResult.startDate(), analysisResult.endDate()
                ).stream()
                .filter(t -> t.getStatus() == TransactionStatus.APPROVED)
                .filter(t -> t.getType() == TransactionType.PURCHASE ||
                        t.getType() == TransactionType.WITHDRAWAL ||
                        t.getType() == TransactionType.TRANSFER)
                .toList();

        byte[] csvBytes = csvExportService.exportExpenseReport(analysisResult, transactions);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=despesas-transacoes.csv")
                .body(csvBytes);
    }

    @Operation(summary = "Consultar saldo atual", description = "Retorna o saldo atual consolidado da conta do utilizador autenticado.")
    @GetMapping("/balance")
    public ResponseEntity<BalanceResponseDto> getBalance() {
        Long userId = getAuthenticatedUserId();
        BalanceResponseDto response = getBalanceUseCase.execute(userId);
        return ResponseEntity.ok(response);
    }
}