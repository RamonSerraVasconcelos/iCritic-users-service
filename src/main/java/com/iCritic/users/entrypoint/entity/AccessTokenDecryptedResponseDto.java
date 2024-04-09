package com.iCritic.users.entrypoint.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AccessTokenDecryptedResponseDto {

    private String decryptedToken;
}
