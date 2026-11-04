package com.financeiro.ms1usuarios.application.ports.in;

import com.financeiro.ms1usuarios.infrastructure.web.dto.CsvImportReportResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImportUsersCsvInboundPort {
    CsvImportReportResponse execute(MultipartFile file);

}
