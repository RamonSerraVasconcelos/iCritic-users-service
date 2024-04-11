package com.iCritic.users.core.usecase;

import com.iCritic.users.core.fixture.AccessTokenFixture;
import com.iCritic.users.core.model.AccessToken;
import com.iCritic.users.core.usecase.boundary.CheckUsersBlacklistBoundary;
import com.iCritic.users.core.usecase.boundary.ValidateAccessTokenBoundary;
import com.iCritic.users.core.usecase.boundary.ValidateDecryptedTokenBoundary;
import com.iCritic.users.entrypoint.fixture.JwtTokenFixture;
import com.iCritic.users.exception.UnauthorizedAccessException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateAccessTokenUseCaseTest {

    @InjectMocks
    private ValidateAccessTokenUseCase validateAccessTokenUseCase;

    @Mock
    private ValidateAccessTokenBoundary validateAccessTokenBoundary;

    @Mock
    private ValidateDecryptedTokenBoundary validateDecryptedTokenBoundary;

    @Mock
    private CheckUsersBlacklistBoundary checkUsersBlacklistBoundary;

    @Test
    void givenValidSignedToken_ThenValidateToken_AndReturnAccessToken() {
        String token = JwtTokenFixture.load();
        AccessToken accessTokenFixture = AccessTokenFixture.load();

        when(validateAccessTokenBoundary.execute(token)).thenReturn(accessTokenFixture);
        when(checkUsersBlacklistBoundary.isUserBlackListed(1L)).thenReturn(false);

        AccessToken accessToken = validateAccessTokenUseCase.execute(token);

        verify(validateAccessTokenBoundary).execute(token);
        verify(checkUsersBlacklistBoundary).isUserBlackListed(1L);
        verifyNoInteractions(validateDecryptedTokenBoundary);

        assertThat(accessToken).isNotNull().usingRecursiveComparison().isEqualTo(accessTokenFixture);
    }

    @Test
    void givenValidUnsignedSignedToken_ThenValidateDecryptedToken_AndReturnAccessToken() {
        String token = JwtTokenFixture.loadUnsignedToken();
        AccessToken accessTokenFixture = AccessTokenFixture.load();

        when(validateDecryptedTokenBoundary.execute(token)).thenReturn(accessTokenFixture);
        when(checkUsersBlacklistBoundary.isUserBlackListed(1L)).thenReturn(false);

        AccessToken accessToken = validateAccessTokenUseCase.execute(token);

        verify(validateDecryptedTokenBoundary).execute(token);
        verify(checkUsersBlacklistBoundary).isUserBlackListed(1L);
        verifyNoInteractions(validateAccessTokenBoundary);

        assertThat(accessToken).isNotNull().usingRecursiveComparison().isEqualTo(accessTokenFixture);
    }

    @Test
    void givenValidToken_WhenUserIsBlacklisted_ThenThrowUnauthorizedAccessException() {
        String token = JwtTokenFixture.loadUnsignedToken();
        AccessToken accessTokenFixture = AccessTokenFixture.load();

        when(validateDecryptedTokenBoundary.execute(token)).thenReturn(accessTokenFixture);
        when(checkUsersBlacklistBoundary.isUserBlackListed(1L)).thenReturn(true);

        UnauthorizedAccessException ex = assertThrows(UnauthorizedAccessException.class, () -> validateAccessTokenUseCase.execute(token));

        verify(validateDecryptedTokenBoundary).execute(token);
        verify(checkUsersBlacklistBoundary).isUserBlackListed(1L);
        verifyNoInteractions(validateAccessTokenBoundary);

        assertThat(ex.getMessage()).isEqualTo("Invalid access token");
    }

    @Test
    void givenInvalidToken_ThenThrowUnauthorizedAccessException() {
        String token = JwtTokenFixture.loadUnsignedToken();

        doThrow(new UnsupportedJwtException("Unsupported Jwt")).when(validateDecryptedTokenBoundary).execute(token);

        UnauthorizedAccessException ex = assertThrows(UnauthorizedAccessException.class, () -> validateAccessTokenUseCase.execute(token));

        verify(validateDecryptedTokenBoundary).execute(token);
        verifyNoInteractions(validateAccessTokenBoundary);
        verifyNoInteractions(validateAccessTokenBoundary);

        assertThat(ex.getMessage()).isEqualTo("Invalid access token");
    }
}