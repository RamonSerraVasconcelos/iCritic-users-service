package com.iCritic.users.core.usecase;

import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.PostEmailResetMessageBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateUserBoundary;
import com.iCritic.users.exception.ResourceNotFoundException;
import com.iCritic.users.exception.ResourceViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailResetUseCaseTest {

    @InjectMocks
    private EmailResetUseCase emailResetUseCase;

    @Mock
    private FindUserByIdUseCase findUserByIdUseCase;

    @Mock
    private UpdateUserBoundary updateUserBoundary;

    @Mock
    private PostEmailResetMessageBoundary postEmailResetMessageBoundary;

    @Mock
    private BCryptPasswordEncoder bcrypt;

    @Test
    void givenValidParameters_thenChangeUserEmail() {
        User user = UserFixture.load();
        user.setEmailResetHash("emailResetHash");
        user.setEmailResetDate(LocalDateTime.now().plusMinutes(10));

        when(findUserByIdUseCase.execute(user.getId())).thenReturn(user);
        when(bcrypt.matches(anyString(), anyString())).thenReturn(true);
        when(updateUserBoundary.execute(any(User.class))).thenReturn(user);

        emailResetUseCase.execute(user.getId(), "newemail@test.test");

        verify(findUserByIdUseCase).execute(user.getId());
        verify(bcrypt).matches(anyString(), anyString());
        verify(updateUserBoundary).execute(any(User.class));
    }

    @Test
    void givenExecutionWithInvalidEmailResetHash_thenThrowResourceViolationException() {
        User user = UserFixture.load();
        user.setEmailResetHash("emailResetHash");
        user.setEmailResetDate(LocalDateTime.now().plusMinutes(10));

        when(findUserByIdUseCase.execute(user.getId())).thenReturn(user);
        when(bcrypt.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(ResourceViolationException.class, () -> emailResetUseCase.execute(user.getId(), "newemail@test.test"));
    }

    @Test
    void givenExecutionWithExpiredEmailResetLink_thenThrowResourceViolationException() {
        User user = UserFixture.load();
        user.setEmailResetHash("emailResetHash");
        user.setEmailResetDate(LocalDateTime.now().minusMinutes(10));

        when(findUserByIdUseCase.execute(user.getId())).thenReturn(user);
        when(bcrypt.matches(anyString(), anyString())).thenReturn(true);

        assertThrows(ResourceViolationException.class, () -> emailResetUseCase.execute(user.getId(), "newemail@test.test"));
    }

    @Test
    void givenResourceNotFoundException_thenThrowResourceViolationException() {
        doThrow(ResourceNotFoundException.class).when(findUserByIdUseCase).execute(any());

        assertThrows(ResourceViolationException.class, () -> emailResetUseCase.execute(1L, "newemail@test.test"));
    }
}