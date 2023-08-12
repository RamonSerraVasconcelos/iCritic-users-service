package com.iCritic.users.core.usecase;

import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUserByEmailBoundary;
import com.iCritic.users.core.usecase.boundary.PostPasswordResetMessageBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateUserBoundary;
import com.iCritic.users.dataprovider.jwt.JwtManager;
import com.iCritic.users.entrypoint.fixture.PasswordResetDataFixture;
import com.iCritic.users.entrypoint.model.PasswordResetData;
import com.iCritic.users.exception.ResourceViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordResetUseCaseTest {

    @InjectMocks
    private PasswordResetUseCase passwordResetUseCase;

    @Mock
    private FindUserByEmailBoundary findUserByEmailBoundary;

    @Mock
    private UpdateUserBoundary updateUserBoundary;

    @Mock
    private PostPasswordResetMessageBoundary postPasswordResetMessageBoundary;

    @Mock
    private BCryptPasswordEncoder bcrypt;

    @Mock
    private JwtManager jwtManager;

    @Test
    void givenValidParameters_thenUpdateUserAndPostPasswordResetMessage() {
        PasswordResetData passwordResetData = PasswordResetDataFixture.load();
        User user = UserFixture.load();
        user.setPasswordResetDate(LocalDateTime.now().plusMinutes(5));

        when(findUserByEmailBoundary.execute(passwordResetData.getEmail())).thenReturn(user);
        when(bcrypt.matches(passwordResetData.getPasswordResetHash(), user.getPasswordResetHash())).thenReturn(true);
        when(bcrypt.matches(passwordResetData.getPassword(), user.getPassword())).thenReturn(false);

        passwordResetUseCase.execute(passwordResetData.getEmail(), passwordResetData.getPasswordResetHash(), passwordResetData.getPassword());

        verify(findUserByEmailBoundary).execute(passwordResetData.getEmail());
        verify(bcrypt).matches(passwordResetData.getPasswordResetHash(), "test");
        verify(bcrypt).matches(passwordResetData.getPassword(), "test");
        verify(postPasswordResetMessageBoundary).execute(user.getId(), user.getEmail());
        verify(updateUserBoundary).execute(user);
        verify(jwtManager).revokeUserTokens(user.getId());
    }

    @Test
    void givenInvalidEmail_thenThrowResourceViolationException() {
        PasswordResetData passwordResetData = PasswordResetDataFixture.load();
        passwordResetData.setEmail(null);

        assertThrows(ResourceViolationException.class, () -> passwordResetUseCase.execute(passwordResetData.getEmail(), passwordResetData.getPasswordResetHash(), passwordResetData.getPassword()));
        verify(findUserByEmailBoundary).execute(passwordResetData.getEmail());
        verifyNoInteractions(bcrypt);
        verifyNoInteractions(postPasswordResetMessageBoundary);
        verifyNoInteractions(updateUserBoundary);
        verifyNoInteractions(jwtManager);
    }

    @Test
    void givenInvalidPasswordResetHash_thenThrowResourceViolationException() {
        PasswordResetData passwordResetData = PasswordResetDataFixture.load();
        passwordResetData.setPasswordResetHash("invalidHash");

        assertThrows(ResourceViolationException.class, () -> passwordResetUseCase.execute(passwordResetData.getEmail(), passwordResetData.getPasswordResetHash(), passwordResetData.getPassword()));
        verify(findUserByEmailBoundary).execute(passwordResetData.getEmail());
        verifyNoInteractions(bcrypt);
        verifyNoInteractions(postPasswordResetMessageBoundary);
        verifyNoInteractions(updateUserBoundary);
        verifyNoInteractions(jwtManager);
    }

    @Test
    void givenOutdatedPasswordResetHash_thenThrowResourceViolationException() {
        PasswordResetData passwordResetData = PasswordResetDataFixture.load();
        User user = UserFixture.load();
        user.setPasswordResetDate(LocalDateTime.now().minusMinutes(5));

        when(findUserByEmailBoundary.execute(passwordResetData.getEmail())).thenReturn(user);
        when(bcrypt.matches(passwordResetData.getPasswordResetHash(), user.getPasswordResetHash())).thenReturn(true);

        assertThrows(ResourceViolationException.class, () -> passwordResetUseCase.execute(passwordResetData.getEmail(), passwordResetData.getPasswordResetHash(), passwordResetData.getPassword()));
        verify(findUserByEmailBoundary).execute(passwordResetData.getEmail());
        verify(bcrypt).matches(passwordResetData.getPasswordResetHash(), "test");
        verifyNoInteractions(postPasswordResetMessageBoundary);
        verifyNoInteractions(updateUserBoundary);
        verifyNoInteractions(jwtManager);
    }

    @Test
    void givenDuplicatedNewPassword_thenThrowResourceViolationException() {
        PasswordResetData passwordResetData = PasswordResetDataFixture.load();
        User user = UserFixture.load();
        user.setPasswordResetDate(LocalDateTime.now().plusMinutes(5));

        when(findUserByEmailBoundary.execute(passwordResetData.getEmail())).thenReturn(user);
        when(bcrypt.matches(passwordResetData.getPasswordResetHash(), user.getPasswordResetHash())).thenReturn(true);
        when(bcrypt.matches(passwordResetData.getPassword(), user.getPassword())).thenReturn(true);

        assertThrows(ResourceViolationException.class, () -> passwordResetUseCase.execute(passwordResetData.getEmail(), passwordResetData.getPasswordResetHash(), passwordResetData.getPassword()));
        verify(findUserByEmailBoundary).execute(passwordResetData.getEmail());
        verify(bcrypt).matches(passwordResetData.getPasswordResetHash(), "test");
        verify(bcrypt).matches(passwordResetData.getPassword(), "test");
        verifyNoInteractions(postPasswordResetMessageBoundary);
        verifyNoInteractions(updateUserBoundary);
        verifyNoInteractions(jwtManager);
    }
}
