package com.iCritic.users.entrypoint.fixture;

import com.iCritic.users.entrypoint.entity.EmailResetData;

public class EmailResetDataFixture {

    public static EmailResetData load() {
        return EmailResetData.builder()
                .userId(1L)
                .emailResetHash("hash")
                .build();
    }
}
