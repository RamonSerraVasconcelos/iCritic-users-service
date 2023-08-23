package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.core.model.RefreshToken;
import com.iCritic.users.dataprovider.gateway.database.entity.RefreshTokenEntity;
import com.iCritic.users.dataprovider.gateway.database.fixture.RefreshTokenEntityFixture;
import com.iCritic.users.dataprovider.gateway.database.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindRefreshTokenByIdGatewayTest {

    @InjectMocks
    private FindRefreshTokenByIdGateway findRefreshTokenByIdGateway;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void givenExecutionWithValidId_thenReturnOptionalRefreshToken() {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntityFixture.load();
        String id = UUID.randomUUID().toString();

        when(refreshTokenRepository.findById(id)).thenReturn(Optional.of(refreshTokenEntity));

        Optional<RefreshToken> refreshToken = findRefreshTokenByIdGateway.execute(id);

        verify(refreshTokenRepository).findById(id);

        assertNotNull(refreshToken);
        assertEquals(refreshTokenEntity.getId(), refreshToken.get().getId());
        assertEquals(refreshTokenEntity.getIssuedAt(), refreshToken.get().getIssuedAt());
        assertEquals(refreshTokenEntity.getExpiresAt(), refreshToken.get().getExpiresAt());
    }
}
