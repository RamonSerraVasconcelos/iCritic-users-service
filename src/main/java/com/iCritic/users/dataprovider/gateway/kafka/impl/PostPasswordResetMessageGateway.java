package com.iCritic.users.dataprovider.gateway.kafka.impl;

import com.iCritic.users.core.usecase.boundary.PostPasswordResetMessageBoundary;
import com.iCritic.users.dataprovider.gateway.kafka.entity.PasswordResetMessage;
import com.iCritic.users.dataprovider.gateway.kafka.producer.PasswordResetMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostPasswordResetMessageGateway implements PostPasswordResetMessageBoundary {

    private final PasswordResetMessageProducer producer;

    public void execute(Long userId, String email) {
        PasswordResetMessage passwordResetMessage = PasswordResetMessage.builder()
                .userId(userId)
                .email(email)
                .build();

        producer.execute(passwordResetMessage);
    }
}
