package com.uvg.earth.way.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "organization")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Organization name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @Email(message = "Provide a valid contact email")
    @NotBlank(message = "Contact email is required")
    private String contactEmail;

    @NotBlank(message = "Contact phone is required")
    private String contactPhone;

    @OneToOne(optional = true)
    @JoinColumn(name = "creator_id", unique = true)
    private User creator;
}
