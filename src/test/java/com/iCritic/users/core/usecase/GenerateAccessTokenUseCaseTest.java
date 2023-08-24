package com.iCritic.users.core.usecase;

import com.iCritic.users.config.properties.ApplicationProperties;
import com.iCritic.users.core.fixture.AccessTokenFixture;
import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.AccessToken;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.GenerateAccessTokenBoundary;
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
class GenerateAccessTokenUseCaseTest {

    @InjectMocks
    private GenerateAccessTokenUseCase generateAccessTokenUseCase;

    @Mock
    private GenerateAccessTokenBoundary generateAccessTokenBoundary;

    @Mock
    private ApplicationProperties applicationProperties;

    @Test
    void givenExecutionWithValidUser_thenGenerateAndReturnAccessToken() {
        User user = UserFixture.load();
        AccessToken accessToken = AccessTokenFixture.load();

        when(applicationProperties.getJwtExpiration()).thenReturn(JwtTokenFixture.EXPIRATION);
        when(generateAccessTokenBoundary.execute(any(AccessToken.class))).thenReturn(accessToken.getEncodedToken());

        AccessToken resultToken = generateAccessTokenUseCase.execute(user);

        verify(applicationProperties).getJwtExpiration();
        verify(generateAccessTokenBoundary).execute(any(AccessToken.class));

        assertNotNull(resultToken);
        assertEquals(accessToken.getEncodedToken(), resultToken.getEncodedToken());
    }
}
