package com.iCritic.users.core.usecase;

import com.iCritic.users.core.fixture.AccessTokenFixture;
import com.iCritic.users.core.fixture.RefreshTokenFixture;
import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.AccessToken;
import com.iCritic.users.core.model.AuthorizationData;
import com.iCritic.users.core.model.Claim;
import com.iCritic.users.core.model.RefreshToken;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.DeleteUserRefreshTokensBoundary;
import com.iCritic.users.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.users.exception.ResourceNotFoundException;
import com.iCritic.users.exception.UnauthorizedAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshUserTokenUseCaseTest {

    @InjectMocks
    private RefreshUserTokenUseCase refreshTokenUseCase;

    @Mock
    private FindUserByIdBoundary findUserByIdBoundary;

    @Mock
    private ValidateRefreshTokenUseCase validateRefreshTokenUseCase;

    @Mock
    private DeleteUserRefreshTokensBoundary deleteUserRefreshTokensBoundary;

    @Mock
    private GenerateAccessTokenUseCase generateAccessTokenUseCase;

    @Mock
    private GenerateRefreshTokenUseCase generateRefreshTokenUseCase;

    @Test
    void givenExecutionWithValidToken_thenDeleteUserSavedTokens_andReturnNewTokens() {
        User user = UserFixture.load();
        AccessToken accessToken = AccessTokenFixture.load();
        RefreshToken refreshToken = RefreshTokenFixture.load();
        refreshToken.setClaims(List.of(
                Claim.builder().name("userId").value("1").build()
        ));

        when(validateRefreshTokenUseCase.execute(refreshToken.getEncodedToken())).thenReturn(refreshToken);
        when(findUserByIdBoundary.execute(user.getId())).thenReturn(user);
        when(generateAccessTokenUseCase.execute(user)).thenReturn(accessToken);
        when(generateRefreshTokenUseCase.execute(user)).thenReturn(refreshToken);

        AuthorizationData authorizationData = refreshTokenUseCase.execute(refreshToken.getEncodedToken());

        verify(validateRefreshTokenUseCase).execute(refreshToken.getEncodedToken());
        verify(findUserByIdBoundary).execute(user.getId());
        verify(deleteUserRefreshTokensBoundary).execute(user.getId());
        verify(generateAccessTokenUseCase).execute(user);
        verify(generateRefreshTokenUseCase).execute(user);

        assertNotNull(authorizationData);
    }

    @Test
    void givenExecutionWithInvalidToken_whenClaimsAreNotPresent_thenUnauthorizedAccessException() {
        RefreshToken refreshToken = RefreshTokenFixture.load();

        when(validateRefreshTokenUseCase.execute(refreshToken.getEncodedToken())).thenReturn(refreshToken);

        assertThrows(UnauthorizedAccessException.class, () -> refreshTokenUseCase.execute(refreshToken.getEncodedToken()));
    }

    @Test
    void givenExecution_whenResourceNotFoundExceptionIsThrown_thenThrowUnauthorizedAccessException() {
        RefreshToken refreshToken = RefreshTokenFixture.load();
        refreshToken.setClaims(List.of(
                Claim.builder().name("userId").value("1").build()
        ));

        when(validateRefreshTokenUseCase.execute(refreshToken.getEncodedToken())).thenReturn(refreshToken);
        doThrow(ResourceNotFoundException.class).when(findUserByIdBoundary).execute(anyLong());

        assertThrows(UnauthorizedAccessException.class, () -> refreshTokenUseCase.execute(refreshToken.getEncodedToken()));
    }
}
