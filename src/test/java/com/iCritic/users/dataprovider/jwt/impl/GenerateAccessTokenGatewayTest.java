package com.iCritic.users.dataprovider.jwt.impl;

import com.iCritic.users.config.properties.ApplicationProperties;
import com.iCritic.users.core.fixture.AccessTokenFixture;
import com.iCritic.users.core.model.AccessToken;
import com.iCritic.users.entrypoint.fixture.JwtTokenFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerateAccessTokenGatewayTest {

    @InjectMocks
    private GenerateAccessTokenGateway generateAccessTokenGateway;

    @Mock
    private ApplicationProperties applicationProperties;

    @Test
    void givenExecutionWithValidAccessToken_thenReturnEncodedToken() {
        AccessToken accessToken = AccessTokenFixture.load();

        when(applicationProperties.getJwtSecret()).thenReturn(JwtTokenFixture.SECRET);

        String token = generateAccessTokenGateway.execute(accessToken);

        verify(applicationProperties).getJwtSecret();

        assertNotNull(token);
    }
}
