package com.iCritic.users.dataprovider.jwt.impl;

import com.iCritic.users.config.properties.ApplicationProperties;
import com.iCritic.users.core.fixture.RefreshTokenFixture;
import com.iCritic.users.core.model.RefreshToken;
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
class GenerateRefreshTokenGatewayTest {

    @InjectMocks
    private GenerateRefreshTokenGateway generateRefreshTokenGateway;

    @Mock
    private ApplicationProperties applicationProperties;

    @Test
    void givenExecutionWithValidRefreshToken_thenReturnEncodedToken() {
        RefreshToken refreshToken = RefreshTokenFixture.load();
        when(applicationProperties.getJwtRefreshSecret()).thenReturn(JwtTokenFixture.REFRESH_SECRET);

        String token = generateRefreshTokenGateway.execute(refreshToken);

        verify(applicationProperties).getJwtRefreshSecret();

        assertNotNull(token);
    }
}
