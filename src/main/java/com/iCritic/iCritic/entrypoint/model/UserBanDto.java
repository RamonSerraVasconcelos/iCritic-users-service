package com.iCritic.iCritic.entrypoint.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserBanDto {

    @NotBlank(message = "Motive is required")
    @Length(min = 4, message = "Motive must be at least 8 characters long")
    private String motive;
}
