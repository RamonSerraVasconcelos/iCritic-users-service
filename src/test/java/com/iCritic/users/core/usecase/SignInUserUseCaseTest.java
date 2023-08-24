package com.iCritic.users.core.usecase;

import com.iCritic.users.core.fixture.AccessTokenFixture;
import com.iCritic.users.core.fixture.RefreshTokenFixture;
import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.AccessToken;
import com.iCritic.users.core.model.AuthorizationData;
import com.iCritic.users.core.model.RefreshToken;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.DeleteUserRefreshTokensBoundary;
import com.iCritic.users.core.usecase.boundary.FindUserByEmailBoundary;
import com.iCritic.users.entrypoint.fixture.AuthorizationDataFixture;
import com.iCritic.users.exception.ResourceViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignInUserUseCaseTest {

    @InjectMocks
    private SignInUserUseCase signInUserUseCase;

    @Mock
    private FindUserByEmailBoundary findUserByEmailBoundary;

    @Mock
    private DeleteUserRefreshTokensBoundary deleteUserRefreshTokensBoundary;

    @Mock
    private GenerateAccessTokenUseCase generateAccessTokenUseCase;

    @Mock
    private GenerateRefreshTokenUseCase generateRefreshTokenUseCase;

    @Mock
    private BCryptPasswordEncoder bcrypt;

    @Test
    void givenValidEmailAndPassword_thenReturnTrue() {
        User user = UserFixture.load();
        User foundUser = UserFixture.load();
        AccessToken accessToken = AccessTokenFixture.load();
        RefreshToken refreshToken = RefreshTokenFixture.load();

        when(findUserByEmailBoundary.execute(user.getEmail())).thenReturn(foundUser);
        when(bcrypt.matches(user.getPassword(), foundUser.getPassword())).thenReturn(true);
        when(generateAccessTokenUseCase.execute(foundUser)).thenReturn(accessToken);
        when(generateRefreshTokenUseCase.execute(foundUser)).thenReturn(refreshToken);

        AuthorizationData returnedAuthData = signInUserUseCase.execute(user);

        verify(findUserByEmailBoundary).execute(user.getEmail());
        verify(bcrypt).matches(user.getPassword(), foundUser.getPassword());
        verify(generateAccessTokenUseCase).execute(foundUser);
        verify(generateRefreshTokenUseCase).execute(foundUser);

        assertNotNull(returnedAuthData);
        assertEquals(accessToken.getEncodedToken(), returnedAuthData.getAccessToken());
        assertEquals(refreshToken.getEncodedToken(), returnedAuthData.getRefreshToken());
    }

    @Test
    void givenInvalidEmail_thenThrowResourceViolationException() {
        User user = UserFixture.load();

        when(findUserByEmailBoundary.execute(user.getEmail())).thenReturn(null);

        assertThrows(ResourceViolationException.class, () -> signInUserUseCase.execute(user));
    }

    @Test
    void givenInvalidPassword_thenThrowResourceViolationException() {
        User user = UserFixture.load();
        User foundUser = UserFixture.load();

        when(findUserByEmailBoundary.execute(user.getEmail())).thenReturn(foundUser);
        when(bcrypt.matches(user.getPassword(), foundUser.getPassword())).thenReturn(false);

        assertThrows(ResourceViolationException.class, () -> signInUserUseCase.execute(user));
    }
}
