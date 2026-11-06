package com.financeiro.ms1usuarios.infrastructure.web.dto;

import java.util.List;

public record CsvImportReportResponse(int totalRecords,
                                      int sucessCount,
                                      int failureCount,
                                      List<CsvErrorDetail> errors) {
}
