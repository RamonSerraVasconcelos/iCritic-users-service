package com.iCritic.iCritic.entrypoint.fixture;

import com.iCritic.iCritic.entrypoint.model.AuthorizationData;

public class AuthorizationDataFixture {

    public static AuthorizationData load() {
        return AuthorizationData.builder()
                .accessToken("accessToken")
                .build();
    }
}
