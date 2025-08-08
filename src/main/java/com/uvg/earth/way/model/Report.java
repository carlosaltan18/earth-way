package com.uvg.earth.way.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.locationtech.jts.geom.Point;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "Report")
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private LocalDate date;

    // TODO: Implementar el dato de tipo Point
    /*@Column(columnDefinition = "geometry(Point, 4326)")
    private Point location;*/

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    private boolean done;

}