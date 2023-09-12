package com.iCritic.users.entrypoint.entity;


import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserBanDto {

    @NotBlank(message = "Motive is required")
    @Length(min = 4, message = "Motive must be at least 8 characters long")
    private String motive;
}
