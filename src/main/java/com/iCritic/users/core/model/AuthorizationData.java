package com.iCritic.users.core.model;

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
