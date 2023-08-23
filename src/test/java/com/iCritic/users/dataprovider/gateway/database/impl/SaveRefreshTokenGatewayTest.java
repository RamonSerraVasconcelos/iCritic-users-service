package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.core.fixture.RefreshTokenFixture;
import com.iCritic.users.core.model.RefreshToken;
import com.iCritic.users.dataprovider.gateway.database.entity.RefreshTokenEntity;
import com.iCritic.users.dataprovider.gateway.database.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SaveRefreshTokenGatewayTest {

    @InjectMocks
    private SaveRefreshTokenGateway saveRefreshTokenGateway;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void givenExecutionWithValidRefreshToken_thenSaveRefreshToken() {
        RefreshToken refreshToken = RefreshTokenFixture.load();

        saveRefreshTokenGateway.execute(refreshToken);

        verify(refreshTokenRepository).save(any(RefreshTokenEntity.class));
    }
}
