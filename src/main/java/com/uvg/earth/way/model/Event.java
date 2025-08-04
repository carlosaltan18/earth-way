package com.uvg.earth.way.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Event name is required")
    private String name;

    @NotBlank(message = "Event description is required")
    private String description;

    @NotBlank(message = "Event direction is required")
    private String direction;

    @Future(message = "Date must be in the future")
    private LocalDate date;

    @Column(columnDefinition = "geography(Point, 4326)")
    private Point location;

    @ManyToOne
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @ManyToMany
    private List<User> participants;

    private boolean finised;
}

