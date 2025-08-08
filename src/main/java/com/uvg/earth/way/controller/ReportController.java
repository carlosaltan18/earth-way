package com.uvg.earth.way.controller;

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

    @RolesAllowed("USER")
    @PostMapping
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        Report newReport = reportService.createReport(report);
        return  new ResponseEntity<>(newReport, HttpStatus.CREATED);
    }

    @RolesAllowed("USER")
    @GetMapping
    public Page<Report> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return reportService.getReports(page, size);
    }

    @RolesAllowed("USER")
    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        Report reportFound = reportService.getReport(id);
        return  new ResponseEntity<>(reportFound, HttpStatus.OK);
    }

    @RolesAllowed("USER")
    @PutMapping("/{id}")
    public ResponseEntity<Report> updateReport(
            @PathVariable Long id,
            @RequestBody Report report
    ) {
        Report updatedReport = reportService.updateReport(id, report);
        return  new ResponseEntity<>(updatedReport, HttpStatus.OK);
    }

    @RolesAllowed("USER")
    @DeleteMapping("/{id}")
    public ResponseEntity<Report> deleteReport(
            @PathVariable Long id
    ) {
        reportService.deleteReport(id);
        return  new ResponseEntity<>(HttpStatus.OK);
    }
}
