package com.iCritic.users.core.usecase;

import com.iCritic.users.config.properties.ApplicationProperties;
import com.iCritic.users.core.fixture.AccessTokenFixture;
import com.iCritic.users.core.fixture.RefreshTokenFixture;
import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.AccessToken;
import com.iCritic.users.core.model.RefreshToken;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.GenerateRefreshTokenBoundary;
import com.iCritic.users.core.usecase.boundary.SaveRefreshTokenBoundary;
import com.iCritic.users.entrypoint.fixture.JwtTokenFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerateRefreshTokenUseCaseTest {

    @InjectMocks
    private GenerateRefreshTokenUseCase generateRefreshTokenUseCase;

    @Mock
    private GenerateRefreshTokenBoundary generateRefreshTokenBoundary;

    @Mock
    private SaveRefreshTokenBoundary saveRefreshTokenBoundary;

    @Mock
    private ApplicationProperties applicationProperties;

    @Test
    void givenExecutionWithValidUser_thenGenerateAndReturnRefreshToken() {
        User user = UserFixture.load();
        RefreshToken refreshToken = RefreshTokenFixture.load();

        when(applicationProperties.getJwtRefreshExpiration()).thenReturn(JwtTokenFixture.EXPIRATION);
        when(generateRefreshTokenBoundary.execute(any(RefreshToken.class))).thenReturn(refreshToken.getEncodedToken());

        RefreshToken resultToken = generateRefreshTokenUseCase.execute(user);

        verify(applicationProperties).getJwtRefreshExpiration();
        verify(generateRefreshTokenBoundary).execute(any(RefreshToken.class));

        assertNotNull(resultToken);
        assertEquals(refreshToken.getEncodedToken(), resultToken.getEncodedToken());
    }
}
