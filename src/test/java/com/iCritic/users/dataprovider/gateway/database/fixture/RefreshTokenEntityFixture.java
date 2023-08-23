package com.iCritic.users.dataprovider.gateway.database.fixture;

import com.iCritic.users.dataprovider.gateway.database.entity.RefreshTokenEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class RefreshTokenEntityFixture {

    public static RefreshTokenEntity load() {
        return RefreshTokenEntity.builder()
                .id(UUID.randomUUID().toString())
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now())
                .active(true)
                .build();
    }
}
