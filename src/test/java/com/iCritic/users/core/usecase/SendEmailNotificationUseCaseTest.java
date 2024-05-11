package com.iCritic.users.core.usecase;

import com.iCritic.users.core.enums.NotificationContentEnum;
import com.iCritic.users.core.model.EmailNotification;
import com.iCritic.users.core.usecase.boundary.SendEmailNotificationBoundary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SendEmailNotificationUseCaseTest {

    @InjectMocks
    private SendEmailNotificationUseCase sendEmailNotificationUseCase;

    @Mock
    private SendEmailNotificationBoundary sendEmailNotificationBoundary;

    @Test
    void givenExecution_ThenPrepare_AndSendEmail() {
        Long userId = 1L;
        String email = "a@a.com";

        Map<String, String> variables = new HashMap<>();
        variables.put("userId", userId.toString());
        variables.put("emailResetHash", "hash");

        sendEmailNotificationUseCase.execute(userId, email, NotificationContentEnum.EMAIL_RESET_REQUEST, variables);

        verify(sendEmailNotificationBoundary).execute(any(EmailNotification.class));
    }
}