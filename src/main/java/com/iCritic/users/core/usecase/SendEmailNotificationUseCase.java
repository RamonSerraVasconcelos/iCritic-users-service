package com.iCritic.users.core.usecase;

import com.iCritic.users.core.enums.NotificationContentEnum;
import com.iCritic.users.core.model.EmailNotification;
import com.iCritic.users.core.usecase.boundary.SendEmailNotificationBoundary;
import com.iCritic.users.core.utils.NotificationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendEmailNotificationUseCase {

    private final SendEmailNotificationBoundary sendEmailNotificationBoundary;

    public void execute(Long userId, String email, NotificationContentEnum notificationContentEnum, Map<String, String> variables) {
        log.info("Sending email notification to user [{}] with subject id: [{}]", userId, notificationContentEnum.getNotificationId());

        String body = NotificationUtils.replaceVariables(notificationContentEnum.getBodyTemplate(), variables);

        EmailNotification emailNotification = new EmailNotification();
        emailNotification.setUserId(userId);
        emailNotification.setEmail(email);
        emailNotification.setNotificationContentEnum(notificationContentEnum);
        emailNotification.setBody(body);

        sendEmailNotificationBoundary.execute(emailNotification);
    }
}
