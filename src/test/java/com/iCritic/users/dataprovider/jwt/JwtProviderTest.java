package com.iCritic.users.dataprovider.jwt;

import com.iCritic.users.config.properties.ApplicationProperties;
import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.entrypoint.fixture.JwtTokenFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    @InjectMocks
    private JwtProvider jwtProvider;

    @Mock
    private JwtManager jwtManager;

    @Mock
    private ApplicationProperties applicationProperties;

    @Test
    void givenUser_generateAndReturnToken() {
        User user = UserFixture.load();

        when(applicationProperties.getJwtExpiration()).thenReturn(JwtTokenFixture.EXPIRATION);
        when(applicationProperties.getJwtSecret()).thenReturn(JwtTokenFixture.SECRET);
        doNothing().when(jwtManager).revokeUserTokens(any());

        String token = jwtProvider.generateToken(user);

        verify(jwtManager).revokeUserTokens(user.getId());

        assertNotNull(token);
        assertThat(token).isNotEmpty();
    }

    @Test
    void givenUser_generateAndReturnRefreshToken() {
        User user = UserFixture.load();

        when(applicationProperties.getJwtRefreshExpiration()).thenReturn(JwtTokenFixture.REFRESH_EXPIRATION);
        when(applicationProperties.getJwtRefreshSecret()).thenReturn(JwtTokenFixture.REFRESH_SECRET);
        doNothing().when(jwtManager).saveRefreshToken(any(), any(), any(), any());

        String token = jwtProvider.generateRefreshToken(user);

        verify(jwtManager).saveRefreshToken(any(), any(), any(), any());

        assertNotNull(token);
        assertThat(token).isNotEmpty();
    }

    @Test
    void givenValidToken_whenValidating_thenReturnTrue() {
        String token = JwtTokenFixture.load();
        when(applicationProperties.getJwtSecret()).thenReturn(JwtTokenFixture.SECRET);

        boolean result = jwtProvider.validateToken(token);

        assertTrue(result);
    }

    @Test
    void givenInvalidToken_whenValidating_thenReturnFalse() {
        String token = "invalid_token";
        when(applicationProperties.getJwtSecret()).thenReturn(JwtTokenFixture.SECRET);

        boolean result = jwtProvider.validateToken(token);

        assertFalse(result);
    }

    @Test
    void givenValidRefreshToken_whenValidating_thenReturnTrue() {
        String token = JwtTokenFixture.loadRefreshToken();
        when(applicationProperties.getJwtRefreshSecret()).thenReturn(JwtTokenFixture.REFRESH_SECRET);
        when(jwtManager.isTokenActive(any())).thenReturn(true);

        boolean result = jwtProvider.validateRefreshToken(token);

        verify(jwtManager).isTokenActive(any());

        assertTrue(result);
    }

    @Test
    void givenInvalidRefreshToken_whenValidating_thenReturnFalse() {
        String token = "invalid_token";
        when(applicationProperties.getJwtSecret()).thenReturn(JwtTokenFixture.REFRESH_SECRET);

        boolean result = jwtProvider.validateToken(token);

        assertFalse(result);
    }

    @Test
    void givenValidRefreshToken_whenTokenIsNotActive_thenReturnFalse() {
        String token = JwtTokenFixture.loadRefreshToken();

        when(applicationProperties.getJwtRefreshSecret()).thenReturn(JwtTokenFixture.REFRESH_SECRET);
        when(jwtManager.isTokenActive(any())).thenReturn(false);
        doNothing().when(jwtManager).revokeUserTokens(any());

        boolean result = jwtProvider.validateRefreshToken(token);

        verify(jwtManager).isTokenActive(any());
        verify(jwtManager).revokeUserTokens(any());

        assertFalse(result);
    }
}
