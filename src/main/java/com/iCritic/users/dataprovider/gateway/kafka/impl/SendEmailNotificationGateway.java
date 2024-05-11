package com.iCritic.users.dataprovider.gateway.kafka.impl;

import com.iCritic.users.core.model.EmailNotification;
import com.iCritic.users.core.usecase.boundary.SendEmailNotificationBoundary;
import com.iCritic.users.dataprovider.gateway.kafka.entity.EmailNotificationMessage;
import com.iCritic.users.dataprovider.gateway.kafka.mapper.EmailNotificationMessageMapper;
import com.iCritic.users.dataprovider.gateway.kafka.producer.SendEmailNotificationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendEmailNotificationGateway implements SendEmailNotificationBoundary {

    private final SendEmailNotificationProducer sendEmailNotificationProducer;

    public void execute(EmailNotification emailNotification) {
        EmailNotificationMessage emailNotificationMessage = EmailNotificationMessageMapper.emailNotificationToEmailNotificationMessage(emailNotification);

        sendEmailNotificationProducer.execute(emailNotificationMessage);
    }
}
