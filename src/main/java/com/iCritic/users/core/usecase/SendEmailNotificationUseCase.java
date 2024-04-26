package com.iCritic.users.core.usecase;

import com.iCritic.users.core.enums.NotificationBodyEnum;
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

    public void execute(Long userId, String email, String subjectId, String subject, NotificationBodyEnum bodyTemplate, Map<String, String> variables) {
        log.info("Sending email notification to user [{}] with subject id: [{}]", userId, subjectId);

        String body = NotificationUtils.replaceVariables(bodyTemplate.getNotificationBodyTemplate(), variables);

        EmailNotification emailNotification = new EmailNotification();
        emailNotification.setUserId(userId);
        emailNotification.setNotificationSubjectId(subjectId);
        emailNotification.setEmail(email);
        emailNotification.setSubject(subject);
        emailNotification.setBody(body);

        sendEmailNotificationBoundary.execute(emailNotification);
    }
}
