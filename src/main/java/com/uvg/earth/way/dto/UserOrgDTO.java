package com.uvg.earth.way.dto;

import com.uvg.earth.way.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserOrgDTO {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phone;

    public static UserOrgDTO fromEntity(User user) {
        return new UserOrgDTO(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPhone()
        );
    }

}
