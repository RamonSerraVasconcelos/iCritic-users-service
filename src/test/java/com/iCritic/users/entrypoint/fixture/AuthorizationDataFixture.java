package com.iCritic.users.entrypoint.fixture;

import com.iCritic.users.core.model.AuthorizationData;

public class AuthorizationDataFixture {

    public static AuthorizationData load() {
        return AuthorizationData.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
    }
}
