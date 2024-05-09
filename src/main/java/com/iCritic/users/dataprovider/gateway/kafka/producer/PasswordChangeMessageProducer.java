package com.iCritic.users.dataprovider.gateway.kafka.producer;

import com.iCritic.users.config.properties.KafkaProperties;
import com.iCritic.users.dataprovider.gateway.kafka.entity.PasswordChangeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PasswordChangeMessageProducer {

    @Autowired
    private KafkaTemplate<String, PasswordChangeMessage> kafkaTemplate;

    @Autowired
    private KafkaProperties kafkaProperties;

    public void execute(PasswordChangeMessage passwordChangeMessage) {
        log.info("Sending message to topic: [{}] with email: [{}]", kafkaProperties.getPasswordChangeTopic(), passwordChangeMessage.getEmail());

        try {
            kafkaTemplate.send(kafkaProperties.getPasswordChangeTopic(), passwordChangeMessage);
        } catch (Exception e) {
            log.error("Error when sending message to topic: [{}]. Error: [{}]", kafkaProperties.getPasswordChangeTopic(), e.getMessage());
        }
    }
}
