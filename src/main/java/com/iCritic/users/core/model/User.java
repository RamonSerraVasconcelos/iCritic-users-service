package com.iCritic.users.core.model;

import com.iCritic.users.core.enums.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String description;
    private boolean active = true;
    private Role role = Role.DEFAULT;
    private Country country;
    private transient Long countryId;
    private String passwordResetHash;
    private Date passwordResetDate;
    private String emailResetHash;
    private Date emailResetDate;
    private String newEmailReset;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
