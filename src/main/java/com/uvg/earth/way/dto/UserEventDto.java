package com.uvg.earth.way.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEventDto {
    private long id;
    private String name;
    private String surname;
    private String email;
    private String phone;
}

