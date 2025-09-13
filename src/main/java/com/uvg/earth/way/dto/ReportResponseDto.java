package com.uvg.earth.way.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReportResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private String author;
    private Boolean done;
}
