package com.iCritic.users.core.usecase;

import com.iCritic.users.core.fixture.RefreshTokenFixture;
import com.iCritic.users.core.model.Claim;
import com.iCritic.users.core.model.RefreshToken;
import com.iCritic.users.core.usecase.boundary.DeleteUserRefreshTokensBoundary;
import com.iCritic.users.core.usecase.boundary.FindRefreshTokenByIdBoundary;
import com.iCritic.users.core.usecase.boundary.ValidateRefreshTokenBoundary;
import com.iCritic.users.exception.UnauthorizedAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateRefreshTokenUseCaseTest {

    @InjectMocks
    private ValidateRefreshTokenUseCase validateRefreshTokenUseCase;

    @Mock
    private ValidateRefreshTokenBoundary validateRefreshTokenBoundary;

    @Mock
    private FindRefreshTokenByIdBoundary findRefreshTokenByIdBoundary;

    @Mock
    private DeleteUserRefreshTokensBoundary deleteUserRefreshTokensBoundary;

    @Test
    void givenExecutionWithValidToken_thenReturnRefreshToken() {
        RefreshToken refreshToken = RefreshTokenFixture.load();
        refreshToken.setActive(true);

        when(validateRefreshTokenBoundary.execute(refreshToken.getEncodedToken())).thenReturn(refreshToken);
        when(findRefreshTokenByIdBoundary.execute(refreshToken.getId())).thenReturn(Optional.of(refreshToken));

        RefreshToken resultToken = validateRefreshTokenUseCase.execute(refreshToken.getEncodedToken());

        verify(validateRefreshTokenBoundary).execute(refreshToken.getEncodedToken());
        verify(findRefreshTokenByIdBoundary).execute(refreshToken.getId());

        assertNotNull(resultToken);
    }

    @Test
    void givenExecutionWithInvalidToken_whenTokenIdIsNull_thenThrowUnauthorizedAccessException() {
        RefreshToken refreshToken = RefreshTokenFixture.load();
        refreshToken.setId(null);

        when(validateRefreshTokenBoundary.execute(refreshToken.getEncodedToken())).thenReturn(refreshToken);

        assertThrows(UnauthorizedAccessException.class, () -> validateRefreshTokenUseCase.execute(refreshToken.getEncodedToken()));

        verifyNoInteractions(findRefreshTokenByIdBoundary);
        verifyNoInteractions(deleteUserRefreshTokensBoundary);
    }

    @Test
    void givenExecutionWithInvalidToken_whenTokenIsNotOnDb_thenThrowUnauthorizedAccessException() {
        RefreshToken refreshToken = RefreshTokenFixture.load();
        refreshToken.setActive(true);

        when(validateRefreshTokenBoundary.execute(refreshToken.getEncodedToken())).thenReturn(refreshToken);
        when(findRefreshTokenByIdBoundary.execute(refreshToken.getId())).thenReturn(Optional.empty());

        assertThrows(UnauthorizedAccessException.class, () -> validateRefreshTokenUseCase.execute(refreshToken.getEncodedToken()));

        verifyNoInteractions(deleteUserRefreshTokensBoundary);
    }

    @Test
    void givenRequestWithInvalidToken_whenTokenIsNotActiveOnDb_thDeleteUserTokens_andThrowUnauthorizedAccessException() {
        RefreshToken refreshToken = RefreshTokenFixture.load();
        refreshToken.setClaims(List.of(
                Claim.builder().name("userId").value("1").build()
        ));

        when(validateRefreshTokenBoundary.execute(refreshToken.getEncodedToken())).thenReturn(refreshToken);
        when(findRefreshTokenByIdBoundary.execute(refreshToken.getId())).thenReturn(Optional.of(refreshToken));

        assertThrows(UnauthorizedAccessException.class, () -> validateRefreshTokenUseCase.execute(refreshToken.getEncodedToken()));

        verify(deleteUserRefreshTokensBoundary).execute(anyLong());
    }
}
