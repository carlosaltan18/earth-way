package com.uvg.earth.way.dto;
import lombok.Data;

@Data
public class ChangePasswordDto {

    private String pastPassword;
    private String newPassword;
    private String confirmPassword;
}