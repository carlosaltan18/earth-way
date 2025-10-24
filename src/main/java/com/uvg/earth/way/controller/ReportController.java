package com.uvg.earth.way.controller;

import com.uvg.earth.way.dto.ReportRequestDto;
import com.uvg.earth.way.dto.ReportResponseDto;
import com.uvg.earth.way.model.Report;
import com.uvg.earth.way.service.ReportService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/report")
public class ReportController {

    private final ReportService reportService;

    private final static String ADMIN = "ADMIN";
    private final static String USER = "USER";
    private final static String ORGANIZATION = "ORGANIZATION";

    @RolesAllowed({ADMIN})
    @PostMapping
    public ResponseEntity<ReportResponseDto> createReport(@RequestBody ReportRequestDto report) {
        ReportResponseDto newReport = reportService.createReport(report);
        return new ResponseEntity<>(newReport, HttpStatus.CREATED);
    }

    @RolesAllowed({USER, ADMIN, ORGANIZATION})
    @GetMapping
    public Page<ReportResponseDto> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return reportService.getReports(page, size);
    }

    @RolesAllowed({ADMIN, USER, ORGANIZATION})
    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDto> getReportById(@PathVariable Long id) {
        ReportResponseDto reportFound = reportService.getReport(id);
        return new ResponseEntity<>(reportFound, HttpStatus.OK);
    }

    @RolesAllowed({ADMIN})
    @PutMapping("/{id}")
    public ResponseEntity<ReportResponseDto> updateReport(
            @PathVariable Long id,
            @RequestBody ReportRequestDto report
    ) {
        ReportResponseDto updatedReport = reportService.updateReport(id, report);
        return new ResponseEntity<>(updatedReport, HttpStatus.OK);
    }

    @RolesAllowed({ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<Report> deleteReport(
            @PathVariable Long id
    ) {
        reportService.deleteReport(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RolesAllowed({ADMIN})
    @PatchMapping("/{id}")
    public ResponseEntity<ReportResponseDto> updateReport(
            @PathVariable Long id
    ) {
        ReportResponseDto report = reportService.changeStatus(id);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}
