package com.uvg.earth.way.service;

import com.uvg.earth.way.dto.ReportRequestDto;
import com.uvg.earth.way.dto.ReportResponseDto;
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
import java.util.Objects;

@AllArgsConstructor
@Service
public class ReportService implements IReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    @Override
    public ReportResponseDto createReport(ReportRequestDto report) {
        Report newReport = new Report();
        newReport.setTitle(report.getTitle());
        newReport.setDescription(report.getDescription());
        newReport.setDate(LocalDate.now());

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) userDetails).getId();

        User user = userRepository.findById(userId).orElse(null);

        newReport.setAuthor(user);
        newReport.setDone(false);

        return reportToResponseDto(reportRepository.save(newReport));
    }

    @Override
    public ReportResponseDto getReport(Long idReport) {
        return reportToResponseDto(Objects.requireNonNull(reportRepository.findById(idReport).orElse(null)));
    }

    @Override
    public Page<ReportResponseDto> getReports(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Report> reports = reportRepository.findAll(pageable);
        return reports.map(this::reportToResponseDto);
    }

    @Override
    public ReportResponseDto updateReport(Long idReport, ReportRequestDto report) {
        Report oldReport = reportRepository.findById(idReport).orElse(null);
        oldReport.setTitle(report.getTitle());
        oldReport.setDescription(report.getDescription());

        return reportToResponseDto(reportRepository.save(oldReport));
    }

    @Override
    public void deleteReport(Long idReport) {
        reportRepository.deleteById(idReport);
    }

    public ReportResponseDto reportToResponseDto(Report report){
        ReportResponseDto reportResponseDto = new ReportResponseDto();
        reportResponseDto.setId(report.getId());
        reportResponseDto.setTitle(report.getTitle());
        reportResponseDto.setDescription(report.getDescription());
        reportResponseDto.setDate(report.getDate());
        reportResponseDto.setAuthor(report.getAuthor().getUsername());
        reportResponseDto.setDone(report.isDone());
        return reportResponseDto;
    }

}
