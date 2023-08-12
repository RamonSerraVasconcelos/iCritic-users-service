package com.iCritic.users.dataprovider.gateway.kafka.producer;

import com.iCritic.users.config.properties.KafkaProperties;
import com.iCritic.users.dataprovider.gateway.kafka.entity.PasswordResetMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PasswordResetMessageProducer {

    @Autowired
    private KafkaTemplate<String, PasswordResetMessage> kafkaTemplate;

    @Autowired
    private KafkaProperties kafkaProperties;

    public void execute(PasswordResetMessage passwordResetMessage) {
        log.info("Sending message to topic: [{}] with email: [{}]", kafkaProperties.getPasswordResetTopic(), passwordResetMessage.getEmail());

        try {
            kafkaTemplate.send(kafkaProperties.getPasswordResetTopic(), passwordResetMessage);
        } catch (Exception e) {
            log.error("Error when sending message to topic: [{}]. Error: [{}]", kafkaProperties.getPasswordResetTopic(), e.getMessage());
        }
    }
}
