package com.uvg.earth.way.dto;

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
public class LoginUserDto {

    @Email(message = "Proporcione un correo válido")
    @NotBlank(message = "Debe proporcionar un correo electrónico")
    private String email;
    @NotBlank(message = "Debe ingresar una contraseña")
    private String password;

}