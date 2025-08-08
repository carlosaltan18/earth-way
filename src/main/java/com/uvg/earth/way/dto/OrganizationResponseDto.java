package com.uvg.earth.way.dto;
import com.uvg.earth.way.model.Organization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationResponseDto {

    private Long id;
    private String name;
    private String description;
    private String contactEmail;
    private String contactPhone;
    private String logo;
    private UserDto creator;

    public OrganizationResponseDto(Long id, String name, String description, String contactEmail,
                                   String contactPhone, String logo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.logo = logo;
    }

}
