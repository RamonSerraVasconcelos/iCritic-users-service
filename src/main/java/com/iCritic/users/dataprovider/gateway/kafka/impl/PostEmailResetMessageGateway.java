package com.iCritic.users.dataprovider.gateway.kafka.impl;

import com.iCritic.users.core.model.EmailReset;
import com.iCritic.users.core.usecase.boundary.PostEmailResetMessageBoundary;
import com.iCritic.users.dataprovider.gateway.kafka.entity.EmailResetMessage;
import com.iCritic.users.dataprovider.gateway.kafka.producer.EmailResetMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostEmailResetMessageGateway implements PostEmailResetMessageBoundary {

    private final EmailResetMessageProducer producer;

    public void execute(EmailReset emailReset) {
        EmailResetMessage emailResetMessage = EmailResetMessage.builder()
                .userId(emailReset.getUserId())
                .email(emailReset.getEmail())
                .build();

        producer.execute(emailResetMessage);
    }
}
