package com.iCritic.users.entrypoint.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChangePasswordDto {

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "New password is required")
    @Length(min = 8, message = "Password must be at least 8 characters long")
    private String newPassword;

    @NotBlank(message = "New password confirmation is required")
    @Length(min = 8, message = "Password must be at least 8 characters long")
    private String confirmPassword;
}
