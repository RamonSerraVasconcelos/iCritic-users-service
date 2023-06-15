package com.iCritic.users.dataprovider.jwt;

import com.iCritic.users.config.properties.ApplicationProperties;
import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.dataprovider.gateway.jwt.JwtProvider;
import com.iCritic.users.entrypoint.fixture.JwtTokenFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    @InjectMocks
    private JwtProvider jwtProvider;

    @Mock
    private ApplicationProperties applicationProperties;

    @Test
    void givenUser_generateAndReturnToken() {
        User user = UserFixture.load();

        when(applicationProperties.getJwtExpiration()).thenReturn(JwtTokenFixture.EXPIRATION);
        when(applicationProperties.getJwtSecret()).thenReturn(JwtTokenFixture.SECRET);

        String token = jwtProvider.generateToken(user);

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
    void givenInvalidToken_whenValidating_thenReturnFalse_AndThrowSignatureException() {
        String token = "invalid_token";
        when(applicationProperties.getJwtSecret()).thenReturn(JwtTokenFixture.SECRET);

        boolean result = jwtProvider.validateToken(token);

        assertFalse(result);
    }
}
