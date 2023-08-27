package com.iCritic.users.entrypoint.fixture;

import com.iCritic.users.entrypoint.entity.PasswordResetData;

public class PasswordResetDataFixture {

    public static PasswordResetData load() {
        return PasswordResetData.builder()
                .email("test@test.test")
                .passwordResetHash("test")
                .password("12345678")
                .build();
    }
}
