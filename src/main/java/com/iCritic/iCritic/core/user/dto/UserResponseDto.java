package com.iCritic.iCritic.core.user.dto;

import com.iCritic.iCritic.core.country.Country;
import com.iCritic.iCritic.core.country.dto.CountryResponseDto;
import com.iCritic.iCritic.core.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String description;
    private Boolean active;
    private Role role;
    private CountryResponseDto country;
    private LocalDateTime createdAt;
}
