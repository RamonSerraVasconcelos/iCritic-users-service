package com.iCritic.users.entrypoint.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthorizationData {

    private String accessToken;
    private String refreshToken;
}
