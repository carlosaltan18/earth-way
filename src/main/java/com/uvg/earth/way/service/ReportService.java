package com.uvg.earth.way.service;

import com.uvg.earth.way.model.Report;
import com.uvg.earth.way.model.User;
import com.uvg.earth.way.repository.ReportRepository;
import com.uvg.earth.way.repository.UserRepository;
import com.uvg.earth.way.service.interfaces.IReportService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Service
public class ReportService implements IReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    @Override
    public Report createReport(Report report) {
        Report newReport = new Report();
        newReport.setTitle(report.getTitle());
        newReport.setDescription(report.getDescription());
        newReport.setDate(LocalDate.now());

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) userDetails).getId();

        User user = userRepository.findById(userId).orElse(null);

        newReport.setAuthor(user);
        newReport.setDone(false);

        return reportRepository.save(newReport);
    }

    @Override
    public Report getReport(Long idReport) {
        return reportRepository.findById(idReport).orElse(null);
    }

    @Override
    public Page<Report> getReports(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return reportRepository.findAll(pageable);
    }

    @Override
    public Report updateReport(Long idReport, Report report) {
        Report oldReport = getReport(idReport);
        oldReport.setTitle(report.getTitle());
        oldReport.setDescription(report.getDescription());

        return reportRepository.save(oldReport);
    }

    @Override
    public void deleteReport(Long idReport) {
        reportRepository.deleteById(idReport);
    }

}
