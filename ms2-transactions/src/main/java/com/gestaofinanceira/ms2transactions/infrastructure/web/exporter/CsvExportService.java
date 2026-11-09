package com.gestaofinanceira.ms2transactions.infrastructure.web.exporter;

import com.gestaofinanceira.ms2transactions.domain.usecase.ExpenseAnalysisUseCase;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

@Service
public class CsvExportService {

    public String exportExpenseAnalysisToCsv(ExpenseAnalysisUseCase.ExpenseAnalysisResult result) {
        StringWriter writer = new StringWriter();
        PrintWriter csvWriter = new PrintWriter(writer);


        csvWriter.println("RELATORIO DE ANALISE DE DESPESAS");
        csvWriter.println("Periodo Inicial," + result.startDate());
        csvWriter.println("Periodo Final," + result.endDate());
        csvWriter.println("Total Geral," + result.totalGeneral());
        csvWriter.println();


        csvWriter.println("=== DESPESAS POR CATEGORIA ===");
        csvWriter.println("Categoria,Valor");
        for (Map.Entry<String, java.math.BigDecimal> entry : result.expensesByCategory().entrySet()) {
            csvWriter.println(entry.getKey() + "," + entry.getValue());
        }
        csvWriter.println();


        csvWriter.println("=== DESPESAS POR DIA ===");
        csvWriter.println("Data,Valor");
        for (Map.Entry<String, java.math.BigDecimal> entry : result.expensesByDay().entrySet()) {
            csvWriter.println(entry.getKey() + "," + entry.getValue());
        }
        csvWriter.println();


        csvWriter.println("=== DESPESAS POR MES ===");
        csvWriter.println("Mes,Valor");
        for (Map.Entry<String, java.math.BigDecimal> entry : result.expensesByMonth().entrySet()) {
            csvWriter.println(entry.getKey() + "," + entry.getValue());
        }

        return writer.toString();
    }

}
