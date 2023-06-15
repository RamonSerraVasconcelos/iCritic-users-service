package com.iCritic.users.entrypoint.fixture;

import com.iCritic.users.entrypoint.model.AuthorizationData;

public class AuthorizationDataFixture {

    public static AuthorizationData load() {
        return AuthorizationData.builder()
                .accessToken("accessToken")
                .build();
    }
}
