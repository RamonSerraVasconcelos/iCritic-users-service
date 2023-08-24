package com.iCritic.users.dataprovider.jwt.impl;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.iCritic.users.config.properties.ApplicationProperties;
import com.iCritic.users.core.model.RefreshToken;
import com.iCritic.users.entrypoint.fixture.JwtTokenFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateRefreshTokenGatewayTest {

    @InjectMocks
    private ValidateRefreshTokenGateway validateRefreshTokenGateway;

    @Mock
    private ApplicationProperties applicationProperties;

    @Test
    void givenExecutionWithValidToken_thenReturnAccessToken() {
        String token = JwtTokenFixture.loadRefreshToken();

        when(applicationProperties.getJwtRefreshSecret()).thenReturn(JwtTokenFixture.REFRESH_SECRET);

        RefreshToken refreshToken = validateRefreshTokenGateway.execute(token);

        assertNotNull(refreshToken);
    }

    @Test
    void givenExecutionWithInvalidToken_thenLogError_andReturnNull() {
        String token = "invalidToken";

        when(applicationProperties.getJwtRefreshSecret()).thenReturn(JwtTokenFixture.REFRESH_SECRET);

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        loggerContext.getLogger("com.iCritic.users.dataprovider.jwt.impl.ValidateRefreshTokenGateway").addAppender(listAppender);

        RefreshToken refreshToken = validateRefreshTokenGateway.execute(token);

        assertNull(refreshToken);

        List<ILoggingEvent> loggingEvents = listAppender.list;
        assertTrue(loggingEvents.toArray().length > 0);
    }
}
