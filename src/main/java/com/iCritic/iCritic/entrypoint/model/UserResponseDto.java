package com.iCritic.iCritic.entrypoint.model;

import com.iCritic.iCritic.entrypoint.model.CountryResponseDto;
import com.iCritic.iCritic.core.enums.Role;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String description;
    private boolean active;
    private Role role;
    private CountryResponseDto country;
    private LocalDateTime createdAt;
}
