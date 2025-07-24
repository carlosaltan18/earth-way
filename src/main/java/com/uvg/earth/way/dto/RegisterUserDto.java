package com.uvg.earth.way.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class RegisterUserDto {

    @NotBlank(message = "Debe proporcionar al menos un nombre")
    private String name;
    @NotBlank(message = "Debe proporcionar al menos un apellido")
    private String surname;
    @Column(unique = true)
    @Email(message = "Proporcione un correo válido")
    @NotBlank(message = "Debe proporcionar un correo electrónico")
    private String email;
    @NotBlank(message = "Debe ingresar una contraseña")
    private String password;
    @NotBlank(message = "Debe ingresar un telefono")
    private String phone;


}