package com.uvg.earth.way.dto;

import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;
import java.util.Map;

@Data
public class ReportResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private String author;
    private Boolean done;
    private Map<String, Double> location;
}
