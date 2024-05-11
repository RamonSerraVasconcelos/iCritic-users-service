package com.iCritic.users.dataprovider.gateway.kafka.mapper;

import com.iCritic.users.core.model.EmailNotification;
import com.iCritic.users.dataprovider.gateway.kafka.entity.EmailNotificationMessage;

public class EmailNotificationMessageMapper {

    public static EmailNotificationMessage emailNotificationToEmailNotificationMessage(EmailNotification emailNotification) {
        return EmailNotificationMessage.builder()
                .userId(emailNotification.getUserId())
                .email(emailNotification.getEmail())
                .notificationSubjectId(emailNotification.getNotificationContentEnum().getNotificationId())
                .subject(emailNotification.getNotificationContentEnum().getSubject())
                .body(emailNotification.getBody())
                .build();
    }
}
