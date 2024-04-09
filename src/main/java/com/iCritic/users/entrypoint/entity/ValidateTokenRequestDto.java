package com.iCritic.users.entrypoint.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ValidateTokenRequestDto {

    @NotBlank(message = "Access token is required")
    private String accessToken;
}
