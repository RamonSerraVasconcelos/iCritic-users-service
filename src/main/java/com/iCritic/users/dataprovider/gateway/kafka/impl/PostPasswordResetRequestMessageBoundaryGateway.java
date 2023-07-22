package com.iCritic.users.dataprovider.gateway.kafka.impl;

import com.iCritic.users.core.model.PasswordResetRequest;
import com.iCritic.users.core.usecase.boundary.PostPasswordResetRequestMessageBoundary;
import com.iCritic.users.dataprovider.gateway.kafka.entity.PasswordResetRequestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostPasswordResetRequestMessageBoundaryGateway implements PostPasswordResetRequestMessageBoundary {

    @Autowired
    private KafkaTemplate<String, PasswordResetRequestMessage> kafkaTemplate;

    @Value("${spring.kafka.password-reset-request-topic}")
    private String topic;

    public void execute(PasswordResetRequest passwordResetRequest) {
        PasswordResetRequestMessage passwordResetRequestMessage = PasswordResetRequestMessage.builder()
                .email(passwordResetRequest.getEmail())
                .passwordResetHash(passwordResetRequest.getPasswordResetHash())
                .build();

        kafkaTemplate.send(topic, passwordResetRequestMessage);
    }
}
