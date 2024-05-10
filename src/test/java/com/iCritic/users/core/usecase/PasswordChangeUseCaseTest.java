package com.iCritic.users.core.usecase;

import com.iCritic.users.core.enums.NotificationBodyEnum;
import com.iCritic.users.core.enums.NotificationIdsEnum;
import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.DeleteUserRefreshTokensBoundary;
import com.iCritic.users.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateUserBoundary;
import com.iCritic.users.exception.ResourceViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordChangeUseCaseTest {

    @InjectMocks
    private PasswordChangeUseCase passwordChangeUseCase;

    @Mock
    private FindUserByIdBoundary findUserByIdBoundary;

    @Mock
    private UpdateUserBoundary updateUserBoundary;

    @Mock
    private DeleteUserRefreshTokensBoundary deleteUserRefreshTokensBoundary;

    @Mock
    private SendEmailNotificationUseCase sendEmailNotificationUseCase;

    @Mock
    private BCryptPasswordEncoder bcrypt;

    @Test
    void givenValidParameters_thenChangeUserPassword_andDeleteUserRefreshTokens() {
        User user = UserFixture.load();
        String newPassword = "newPassword";

        when(findUserByIdBoundary.execute(user.getId())).thenReturn(user);
        when(bcrypt.matches(user.getPassword(), user.getPassword())).thenReturn(true);
        when(bcrypt.matches(newPassword, user.getPassword())).thenReturn(false);
        when(bcrypt.encode(newPassword)).thenReturn("encodedPassword");
        when(updateUserBoundary.execute(user)).thenReturn(user);

        passwordChangeUseCase.execute(user.getId(), user.getPassword(), newPassword, newPassword);

        verify(findUserByIdBoundary).execute(user.getId());
        verify(bcrypt).matches("test", "test");
        verify(bcrypt).matches(newPassword, "test");
        verify(bcrypt).encode(newPassword);
        verify(updateUserBoundary).execute(user);
        verify(deleteUserRefreshTokensBoundary).execute(user.getId());
        verify(sendEmailNotificationUseCase).execute(user.getId(), user.getEmail(), NotificationIdsEnum.PASSWORD_CHANGE.getNotificationId(),
                "Password Reset Request", NotificationBodyEnum.PASSWORD_CHANGE, null);
    }

    @Test
    void givenInvalidCurrentPassword_thenThrowResourceViolationException() {
        User user = UserFixture.load();
        String newPassword = "newPassword";

        when(findUserByIdBoundary.execute(user.getId())).thenReturn(user);
        when(bcrypt.matches(user.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(ResourceViolationException.class, () -> passwordChangeUseCase.execute(user.getId(), user.getPassword(), newPassword, newPassword));

        verify(findUserByIdBoundary).execute(user.getId());
        verify(bcrypt).matches("test", "test");

        verifyNoInteractions(updateUserBoundary);
        verifyNoInteractions(deleteUserRefreshTokensBoundary);
        verifyNoInteractions(sendEmailNotificationUseCase);
    }

    @Test
    void givenExecution_whenNewPasswordIsEqualToCurrentOne_thenThrowResourceViolationException() {
        User user = UserFixture.load();
        String newPassword = "newPassword";

        when(findUserByIdBoundary.execute(user.getId())).thenReturn(user);
        when(bcrypt.matches(user.getPassword(), user.getPassword())).thenReturn(true);
        when(bcrypt.matches(newPassword, user.getPassword())).thenReturn(true);

        ResourceViolationException ex = assertThrows(ResourceViolationException.class, () -> passwordChangeUseCase.execute(user.getId(), user.getPassword(), newPassword, newPassword));

        verify(findUserByIdBoundary).execute(user.getId());
        verify(bcrypt).matches("test", "test");
        verify(bcrypt).matches(newPassword, "test");

        verifyNoInteractions(updateUserBoundary);
        verifyNoInteractions(deleteUserRefreshTokensBoundary);
        verifyNoInteractions(sendEmailNotificationUseCase);

        assertEquals("The new password cannot be equal to the old one", ex.getMessage());
    }

    @Test
    void givenExecution_whenPasswordConfirmationDoesNotMatch_thenThrowResourceViolationException() {
        User user = UserFixture.load();
        String newPassword = "newPassword";

        when(findUserByIdBoundary.execute(user.getId())).thenReturn(user);
        when(bcrypt.matches(user.getPassword(), user.getPassword())).thenReturn(true);
        when(bcrypt.matches(newPassword, user.getPassword())).thenReturn(false);

        ResourceViolationException ex = assertThrows(ResourceViolationException.class, () -> passwordChangeUseCase.execute(user.getId(), user.getPassword(), newPassword, "testing"));

        verify(findUserByIdBoundary).execute(user.getId());
        verify(bcrypt).matches("test", "test");
        verify(bcrypt).matches(newPassword, "test");

        verifyNoInteractions(updateUserBoundary);
        verifyNoInteractions(deleteUserRefreshTokensBoundary);
        verifyNoInteractions(sendEmailNotificationUseCase);

        assertEquals("Password confirmation does not match", ex.getMessage());
    }
}