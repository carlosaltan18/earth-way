package com.uvg.earth.way.dto;

import lombok.Data;

@Data
public class ReportRequestDto {
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;
}
