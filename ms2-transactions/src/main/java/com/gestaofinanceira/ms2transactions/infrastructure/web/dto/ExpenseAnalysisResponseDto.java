package com.gestaofinanceira.ms2transactions.infrastructure.web.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ExpenseAnalysisResponseDto(String startDate,
                                         String endDate,
                                         BigDecimal totalGeneral,
                                         Map<String, BigDecimal> expensesByCategory,
                                         Map<String, BigDecimal> expensesByDay,
                                         Map<String, BigDecimal> expensesByMonth) {
}
