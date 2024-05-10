package com.iCritic.users.core.usecase;

import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUserByEmailBoundary;
import com.iCritic.users.dataprovider.gateway.database.impl.UpdateUserGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PasswordResetRequestUseCaseTest {

    @InjectMocks
    private PasswordResetRequestUseCase passwordResetRequestUseCase;

    @Mock
    private FindUserByEmailBoundary findUserByEmailBoundary;

    @Mock
    private UpdateUserGateway updateUserBoundary;

    @Mock
    private SendEmailNotificationUseCase sendEmailNotificationUseCase;

    @Mock
    private BCryptPasswordEncoder bcrypt;

    @Test
    void givenValidEmail_thenUpdateUser_andSendMessage() {
        User user = UserFixture.load();
        String encodedHash = "2d6h2d0shf@837fs92#123";

        when(bcrypt.encode(anyString())).thenReturn(encodedHash);
        when(findUserByEmailBoundary.execute(user.getEmail())).thenReturn(user);
        when(updateUserBoundary.execute(user)).thenReturn(user);

        passwordResetRequestUseCase.execute(user.getEmail());

        verify(bcrypt).encode(anyString());
        verify(findUserByEmailBoundary).execute(user.getEmail());
        verify(updateUserBoundary).execute(user);
        verify(sendEmailNotificationUseCase).execute(anyLong(), anyString(), anyString(), anyString(), any(), anyMap());
    }

    @Test
    void givenInvalidEmail_thenThrowResourceNotFoundException_andDoNothing() {
        String email = "test@test.test";
        when(findUserByEmailBoundary.execute(email)).thenReturn(null);

        passwordResetRequestUseCase.execute(email);
        verify(findUserByEmailBoundary).execute(email);
        verifyNoInteractions(bcrypt);
        verifyNoInteractions(updateUserBoundary);
        verifyNoInteractions(sendEmailNotificationUseCase);
    }
}
