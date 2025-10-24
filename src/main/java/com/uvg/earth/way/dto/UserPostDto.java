package com.uvg.earth.way.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPostDto {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phone;
}
