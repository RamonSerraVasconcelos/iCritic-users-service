package com.iCritic.users.dataprovider.gateway.kafka.impl;

import com.iCritic.users.core.model.PasswordResetRequest;
import com.iCritic.users.core.usecase.boundary.PostPasswordResetRequestMessageBoundary;
import com.iCritic.users.dataprovider.gateway.kafka.entity.PasswordResetRequestMessage;
import com.iCritic.users.dataprovider.gateway.kafka.producer.PasswordResetRequestMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostPasswordResetRequestMessageGateway implements PostPasswordResetRequestMessageBoundary {

    private final PasswordResetRequestMessageProducer producer;

    public void execute(PasswordResetRequest passwordResetRequest) {
        PasswordResetRequestMessage passwordResetRequestMessage = PasswordResetRequestMessage.builder()
                .userId(passwordResetRequest.getUserId())
                .email(passwordResetRequest.getEmail())
                .passwordResetHash(passwordResetRequest.getPasswordResetHash())
                .build();

        producer.execute(passwordResetRequestMessage);
    }
}
