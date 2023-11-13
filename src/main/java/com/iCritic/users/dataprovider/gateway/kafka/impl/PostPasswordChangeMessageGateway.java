package com.iCritic.users.dataprovider.gateway.kafka.impl;

import com.iCritic.users.core.usecase.boundary.PostPasswordChangeMessageBoundary;
import com.iCritic.users.dataprovider.gateway.kafka.entity.PasswordChangeMessage;
import com.iCritic.users.dataprovider.gateway.kafka.producer.PasswordChangeMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostPasswordChangeMessageGateway implements PostPasswordChangeMessageBoundary {

    private final PasswordChangeMessageProducer producer;

    public void execute(Long userId, String email) {
        PasswordChangeMessage passwordChangeMessage = PasswordChangeMessage.builder()
                .userId(userId)
                .email(email)
                .build();

        producer.execute(passwordChangeMessage);
    }
}
