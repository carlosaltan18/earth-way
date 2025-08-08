package com.uvg.earth.way.dto;

import com.uvg.earth.way.model.Organization;
import com.uvg.earth.way.model.User;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventDto {

    private Long id;

    @NotBlank(message = "Event name is required")
    private String name;

    @NotBlank(message = "Event description is required")
    private String description;

    @NotBlank(message = "Event direction is required")
    private String direction;

    @Future(message = "Date must be in the future")
    private LocalDate date;

    private Point location;


    // DTOs for relations
    private  Long idOrganization;
    private Long idOrganizer;
    private List<UserEventDto> participants;

    private boolean finished;
}




