package com.iCritic.users.entrypoint.entity;

import com.iCritic.users.core.enums.Role;
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
    private String profilePictureUrl;
    private LocalDateTime createdAt;
}
