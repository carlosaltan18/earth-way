package com.uvg.earth.way.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phone;
}