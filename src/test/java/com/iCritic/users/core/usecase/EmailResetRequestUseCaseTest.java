package com.iCritic.users.core.usecase;

import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUserByEmailBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateUserBoundary;
import com.iCritic.users.exception.ResourceConflictException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailResetRequestUseCaseTest {

    @InjectMocks
    private EmailResetRequestUseCase emailResetRequestUseCase;

    @Mock
    private FindUserByIdUseCase findUserByIdUseCase;

    @Mock
    private FindUserByEmailBoundary findUserByEmailBoundary;

    @Mock
    private UpdateUserBoundary updateUserBoundary;

    @Mock
    private SendEmailNotificationUseCase sendEmailNotificationUseCase;

    @Mock
    private BCryptPasswordEncoder bcrypt;

    @Test
    void givenExecutionWithValidParameters_thenUpdateUser_andPostMessage() {
        User user = UserFixture.load();
        String newEmail = "newemail@test.test";
        String encryptedHash = "1&NDSJAN@*#$&@BN";

        when(findUserByIdUseCase.execute(user.getId())).thenReturn(user);
        when(bcrypt.encode(anyString())).thenReturn(encryptedHash);
        when(updateUserBoundary.execute(any(User.class))).thenReturn(user);
        when(findUserByEmailBoundary.execute(newEmail)).thenReturn(null);

        emailResetRequestUseCase.execute(user.getId(), newEmail);

        verify(findUserByIdUseCase).execute(user.getId());
        verify(bcrypt).encode(anyString());
        verify(updateUserBoundary).execute(any(User.class));
        verify(sendEmailNotificationUseCase).execute(anyLong(), anyString(), anyString(), anyString(), any(), anyMap());
    }

    @Test
    void givenExecutionWithValidParameters_whenNewEmailIsEqualToCurrentOne_thenThrowResourceConflictException() {
        User user = UserFixture.load();
        when(findUserByIdUseCase.execute(user.getId())).thenReturn(user);

        assertThrows(ResourceConflictException.class, () -> emailResetRequestUseCase.execute(user.getId(), user.getEmail()));
    }

    @Test
    void givenExecutionWithValidParameters_whenEmailIsAlreadyRegistered_thenThrowResourceConflictException() {
        User user = UserFixture.load();
        when(findUserByIdUseCase.execute(user.getId())).thenReturn(user);
        when(findUserByEmailBoundary.execute(user.getEmail())).thenReturn(user);

        assertThrows(ResourceConflictException.class, () -> emailResetRequestUseCase.execute(user.getId(), user.getEmail()));
    }
}
