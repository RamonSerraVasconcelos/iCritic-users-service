package com.iCritic.users.core.fixture;

import com.iCritic.users.core.model.EmailResetRequest;

import java.util.UUID;

public class EmailResetRequestFixture {

    public static EmailResetRequest load() {
        return EmailResetRequest.builder()
                .userId(1L)
                .email("test@test.test")
                .emailResetHash(UUID.randomUUID().toString())
                .build();
    }
}
