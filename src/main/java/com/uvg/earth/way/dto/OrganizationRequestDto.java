package com.uvg.earth.way.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationRequestDto {
    @NotBlank(message = "El nombre de la organizacion es requerido")
    private String name;

    @NotBlank(message = "La descripcion es requerida")
    private String description;

    @Email(message = "Ingrese un contacto de email válido")
    @NotBlank(message = "El contacto de email es requerido")
    private String contactEmail;

    @NotBlank(message = "El número de celular es requerido")
    private String contactPhone;

    private String logo;
}
