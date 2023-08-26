package com.iCritic.users.dataprovider.gateway.kafka.producer;

import com.iCritic.users.config.properties.KafkaProperties;
import com.iCritic.users.dataprovider.gateway.kafka.entity.EmailResetRequestMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailResetRequestMessageProducer {

    @Autowired
    private KafkaTemplate<String, EmailResetRequestMessage> kafkaTemplate;

    @Autowired
    private KafkaProperties kafkaProperties;

    public void execute(EmailResetRequestMessage emailResetRequestMessage) {
        log.info("Sending message to topic: [{}] with email: [{}]", kafkaProperties.getEmailResetRequestTopic(), emailResetRequestMessage.getEmail());

        try {
            kafkaTemplate.send(kafkaProperties.getEmailResetRequestTopic(), emailResetRequestMessage);
        } catch (Exception e) {
            log.error("Error when sending message to topic: [{}]. Error: [{}]", kafkaProperties.getEmailResetRequestTopic(), e.getMessage());
        }
    }
}
