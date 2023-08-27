package com.iCritic.users.dataprovider.gateway.kafka.producer;

import com.iCritic.users.config.properties.KafkaProperties;
import com.iCritic.users.dataprovider.gateway.kafka.entity.EmailResetMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailResetMessageProducer {

    @Autowired
    private KafkaTemplate<String, EmailResetMessage> kafkaTemplate;

    @Autowired
    private KafkaProperties kafkaProperties;

    public void execute(EmailResetMessage emailResetMessage) {
        log.info("Sending message to topic: [{}] with email: [{}]", kafkaProperties.getEmailResetTopic(), emailResetMessage.getEmail());

        try {
            kafkaTemplate.send(kafkaProperties.getEmailResetTopic(), emailResetMessage);
        } catch (Exception e) {
            log.error("Error when sending message to topic: [{}]. Error: [{}]", kafkaProperties.getEmailResetTopic(), e.getMessage());
        }
    }
}
