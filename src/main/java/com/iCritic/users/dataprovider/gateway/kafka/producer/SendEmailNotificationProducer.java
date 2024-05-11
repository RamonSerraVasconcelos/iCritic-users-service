package com.iCritic.users.dataprovider.gateway.kafka.producer;

import com.iCritic.users.config.properties.ApplicationProperties;
import com.iCritic.users.config.properties.KafkaProperties;
import com.iCritic.users.dataprovider.gateway.kafka.entity.EmailNotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendEmailNotificationProducer {

    @Autowired
    private KafkaTemplate<String, EmailNotificationMessage> kafkaTemplate;

    @Autowired
    private KafkaProperties kafkaProperties;

    public void execute(EmailNotificationMessage emailNotificationMessage) {
        log.info("Sending email notification message with subjectId: [{}] and userId: [{}]", emailNotificationMessage.getNotificationSubjectId(),
                emailNotificationMessage.getUserId());

        try {
            kafkaTemplate.send(kafkaProperties.getEmailNotificationTopic(), emailNotificationMessage);
        } catch (Exception e) {
            log.error("Error when sending email notification message. Error: [{}]", e.getMessage());
        }
    }
}
