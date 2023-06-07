package com.iCritic.iCritic.entrypoint.validation;

import com.iCritic.iCritic.config.properties.ApplicationProperties;
import com.iCritic.iCritic.core.fixture.UserFixture;
import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.entrypoint.fixture.JwtTokenFixture;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtGeneratorTest {

    @InjectMocks
    private JwtGenerator jwtGenerator;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private Logger logger;

    @Test
    void givenUser_generateAndReturnToken() {
        User user = UserFixture.load();

        when(applicationProperties.getJwtExpiration()).thenReturn(JwtTokenFixture.EXPIRATION);
        when(applicationProperties.getJwtSecret()).thenReturn(JwtTokenFixture.SECRET);

        String token = jwtGenerator.generateToken(user);

        assertNotNull(token);
        assertThat(token).isNotEmpty();
    }

    @Test
    void givenValidToken_whenValidating_thenReturnTrue() {
        String token = JwtTokenFixture.load();
        when(applicationProperties.getJwtSecret()).thenReturn(JwtTokenFixture.SECRET);

        boolean result = jwtGenerator.validateToken(token);

        assertTrue(result);
    }

    @Test
    void givenInvalidToken_whenValidating_thenReturnFalse_AndThrowSignatureException() {
        String token = "invalid_token";
        when(applicationProperties.getJwtSecret()).thenReturn(JwtTokenFixture.SECRET);

        boolean result = jwtGenerator.validateToken(token);

        assertFalse(result);
    }
}
