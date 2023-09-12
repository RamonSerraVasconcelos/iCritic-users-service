package com.iCritic.users.dataprovider.gateway.kafka.impl;

import com.iCritic.users.core.model.EmailResetRequest;
import com.iCritic.users.core.usecase.boundary.PostEmailResetRequestMessageBoundary;
import com.iCritic.users.dataprovider.gateway.kafka.entity.EmailResetRequestMessage;
import com.iCritic.users.dataprovider.gateway.kafka.producer.EmailResetRequestMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostEmailResetRequestMessageGateway implements PostEmailResetRequestMessageBoundary {

    private final EmailResetRequestMessageProducer producer;

    public void execute(EmailResetRequest emailResetRequest) {
        EmailResetRequestMessage emailResetRequestMessage = EmailResetRequestMessage.builder()
                .userId(emailResetRequest.getUserId())
                .email(emailResetRequest.getEmail())
                .emailResetHash(emailResetRequest.getEmailResetHash())
                .build();

        producer.execute(emailResetRequestMessage);
    }
}
