package com.uvg.earth.way.service.interfaces;

import com.uvg.earth.way.model.Report;
import org.springframework.data.domain.Page;

public interface IReportService {
    Report createReport(Report report);
    Report getReport(Long idReport);
    Page<Report> getReports(int page, int pageSize);
    Report updateReport(Long idReport, Report report);
    void deleteReport(Long idReport);
}
