package com.iCritic.users.core.fixture;

import com.iCritic.users.core.model.AccessToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AccessTokenFixture {

    public static AccessToken load() {
        return AccessToken.builder()
                .encodedToken("encodedToken")
                .id(UUID.randomUUID().toString())
                .claims(List.of())
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now())
                .build();
    }
}
