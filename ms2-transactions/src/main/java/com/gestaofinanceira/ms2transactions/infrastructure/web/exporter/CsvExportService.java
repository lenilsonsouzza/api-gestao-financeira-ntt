package com.gestaofinanceira.ms2transactions.infrastructure.web.exporter;

import com.gestaofinanceira.ms2transactions.domain.model.Transaction;
import com.gestaofinanceira.ms2transactions.domain.usecase.ExpenseAnalysisUseCase;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class CsvExportService {

    public byte[] exportExpenseReport(ExpenseAnalysisUseCase.ExpenseAnalysisResult analysis, List<Transaction> transactions) {
        StringBuilder csv = new StringBuilder("\uFEFF"); // BOM UTF-8 para acentos no Excel

        // 1. Cabeçalho Executivo do Relatório
        csv.append("RELATORIO DE DESPESAS\n");
        csv.append("Periodo Inicial;").append(analysis.startDate()).append("\n");
        csv.append("Periodo Final;").append(analysis.endDate()).append("\n");
        csv.append("Total Geral;").append(formatMoney(analysis.totalGeneral())).append("\n\n");

        // 2. Resumo por Categoria
        csv.append("=== RESUMO POR CATEGORIA ===\n");
        csv.append("Categoria;Total\n");
        for (Map.Entry<String, BigDecimal> entry : analysis.expensesByCategory().entrySet()) {
            csv.append(field(entry.getKey())).append(";")
                    .append(formatMoney(entry.getValue())).append("\n");
        }
        csv.append("\n");

        // 3. Resumo por Dia
        csv.append("=== RESUMO POR DIA ===\n");
        csv.append("Data;Total\n");
        for (Map.Entry<String, BigDecimal> entry : analysis.expensesByDay().entrySet()) {
            csv.append(entry.getKey()).append(";")
                    .append(formatMoney(entry.getValue())).append("\n");
        }
        csv.append("\n");

        // 4. Resumo por Mês
        csv.append("=== RESUMO POR MES ===\n");
        csv.append("Mes;Total\n");
        for (Map.Entry<String, BigDecimal> entry : analysis.expensesByMonth().entrySet()) {
            csv.append(entry.getKey()).append(";")
                    .append(formatMoney(entry.getValue())).append("\n");
        }
        csv.append("\n");

        // 5. Transações Detalhadas (a listagem linha a linha que já tinhas)
        csv.append("=== DETALHE DAS TRANSACOES ===\n");
        csv.append("data;tipo;categoria;descricao;valor;status;motivo\n");

        for (Transaction t : transactions) {
            csv.append(t.getTimestamp() != null ? t.getTimestamp().toString().replace('T', ' ') : "").append(";")
                    .append(field(t.getType() != null ? t.getType().name() : "")).append(";")
                    .append(field(t.getCategory())).append(";")
                    .append(field(t.getDescription())).append(";")
                    .append(formatMoney(t.getAmount())).append(";")
                    .append(t.getStatus() != null ? t.getStatus().name() : "").append(";")
                    .append(field(t.getRejectionReason())).append('\n');
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String formatMoney(BigDecimal value) {
        if (value == null) return "0,00";
        return value.toPlainString().replace('.', ',');
    }

    private String field(String value) {
        if (value == null) return "";
        String v = value.replace("\"", "\"\"");
        return "\"" + v + "\"";
    }
}