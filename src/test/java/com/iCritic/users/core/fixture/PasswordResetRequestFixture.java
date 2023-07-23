package com.iCritic.users.core.fixture;

import com.iCritic.users.core.model.PasswordResetRequest;

import java.util.UUID;

public class PasswordResetRequestFixture {

    public static PasswordResetRequest load() {
        return PasswordResetRequest.builder()
                .email("test@test.test")
                .passwordResetHash(UUID.randomUUID().toString())
                .build();
    }
}
