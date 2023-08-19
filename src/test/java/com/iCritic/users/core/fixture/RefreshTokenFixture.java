package com.iCritic.users.core.fixture;

import com.iCritic.users.core.model.RefreshToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class RefreshTokenFixture {

    public static RefreshToken load() {
        return RefreshToken.builder()
                .encodedToken("encodedToken")
                .id(UUID.randomUUID().toString())
                .claims(List.of())
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now())
                .build();
    }
}
