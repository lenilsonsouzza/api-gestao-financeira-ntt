package com.gestaofinanceira.ms2transactions.domain.usecase;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;
import com.gestaofinanceira.ms2transactions.domain.model.TransactionStatus;
import com.gestaofinanceira.ms2transactions.domain.model.TransactionType;
import com.gestaofinanceira.ms2transactions.domain.ports.out.TransactionRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ExpenseAnalysisUseCase {

    private final TransactionRepositoryPort transactionRepositoryPort;

    public ExpenseAnalysisResult execute(Long userId, LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        }
        List<Transaction> transactions = transactionRepositoryPort.findByUserIdAndPeriod(userId, startDate, endDate);
        List<Transaction> expenses = transactions.stream()

                .filter(t -> t.getStatus() == TransactionStatus.APPROVED)
                .filter(t -> t.getType() == TransactionType.PURCHASE ||
                        t.getType() == TransactionType.WITHDRAWAL ||
                        t.getType() == TransactionType.TRANSFER)
                .toList();

        BigDecimal totalGeneral = expenses.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        Map<String, BigDecimal> byCategory = expenses.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory() != null ? t.getCategory() : "Outros",
                        Collectors.mapping(Transaction::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        Map<String, BigDecimal> byDay = expenses.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getTimestamp().toLocalDate().toString(),
                        Collectors.mapping(Transaction::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        Map<String, BigDecimal> byMonth = expenses.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getTimestamp().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.mapping(Transaction::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        return new ExpenseAnalysisResult(startDate, endDate, totalGeneral, byCategory, byDay, byMonth);
    }
    public record ExpenseAnalysisResult(
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal totalGeneral,
            Map<String, BigDecimal> expensesByCategory,
            Map<String, BigDecimal> expensesByDay,
            Map<String, BigDecimal> expensesByMonth
    ) {}
}
