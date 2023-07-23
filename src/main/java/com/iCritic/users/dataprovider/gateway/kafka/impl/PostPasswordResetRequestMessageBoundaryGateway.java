package com.iCritic.users.dataprovider.gateway.kafka.impl;

import com.iCritic.users.config.properties.KafkaProperties;
import com.iCritic.users.core.model.PasswordResetRequest;
import com.iCritic.users.core.usecase.boundary.PostPasswordResetRequestMessageBoundary;
import com.iCritic.users.dataprovider.gateway.kafka.entity.PasswordResetRequestMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PostPasswordResetRequestMessageBoundaryGateway implements PostPasswordResetRequestMessageBoundary {

    @Autowired
    private KafkaTemplate<String, PasswordResetRequestMessage> kafkaTemplate;

    @Autowired
    private KafkaProperties kafkaProperties;

    public void execute(PasswordResetRequest passwordResetRequest) {
        try {
            log.info("Sending message to topic: [{}] with email: [{}]", kafkaProperties.getPasswordResetRequestTopic(), passwordResetRequest.getEmail());

            PasswordResetRequestMessage passwordResetRequestMessage = PasswordResetRequestMessage.builder()
                    .email(passwordResetRequest.getEmail())
                    .passwordResetHash(passwordResetRequest.getPasswordResetHash())
                    .build();

            kafkaTemplate.send(kafkaProperties.getPasswordResetRequestTopic(), passwordResetRequestMessage);
        } catch (Exception e) {
            log.error("Error when sending message to topic: [{}]. Error: [{}]", kafkaProperties.getPasswordResetRequestTopic(), e.getMessage());
        }
    }
}
