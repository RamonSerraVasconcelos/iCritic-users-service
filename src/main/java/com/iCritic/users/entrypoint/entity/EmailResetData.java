package com.iCritic.users.entrypoint.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmailResetData {

    @NotNull(message = "User id is required")
    @Min(value = 1, message = "Invalid user id")
    private Long userId;

    @NotBlank(message = "Email reset hash is required")
    private String emailResetHash;
}
