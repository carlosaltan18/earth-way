package com.uvg.earth.way.service.interfaces;

import com.uvg.earth.way.dto.ReportRequestDto;
import com.uvg.earth.way.dto.ReportResponseDto;
import com.uvg.earth.way.model.Report;
import org.springframework.data.domain.Page;

public interface IReportService {
    ReportResponseDto createReport(ReportRequestDto report);
    ReportResponseDto getReport(Long idReport);
    Page<ReportResponseDto> getReports(int page, int pageSize);
    ReportResponseDto updateReport(Long idReport, ReportRequestDto report);
    void deleteReport(Long idReport);
    ReportResponseDto changeStatus(Long idReport);
}
