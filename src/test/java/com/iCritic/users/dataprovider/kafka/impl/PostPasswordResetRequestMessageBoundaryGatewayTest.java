package com.iCritic.users.dataprovider.kafka.impl;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.iCritic.users.config.properties.KafkaProperties;
import com.iCritic.users.core.fixture.PasswordResetRequestFixture;
import com.iCritic.users.core.model.PasswordResetRequest;
import com.iCritic.users.dataprovider.gateway.kafka.entity.PasswordResetRequestMessage;
import com.iCritic.users.dataprovider.gateway.kafka.impl.PostPasswordResetRequestMessageBoundaryGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostPasswordResetRequestMessageBoundaryGatewayTest {

    @InjectMocks
    private PostPasswordResetRequestMessageBoundaryGateway passwordResetRequestGateway;

    @Mock
    private KafkaTemplate<String, PasswordResetRequestMessage> kafkaTemplate;

    @Mock
    KafkaProperties kafkaProperties;

    @Test
    void given_PasswordResetRequest_when_executed_then_send_message() {
        PasswordResetRequest passwordResetRequest = PasswordResetRequestFixture.load();

        when(kafkaProperties.getPasswordResetRequestTopic()).thenReturn("topic");

        passwordResetRequestGateway.execute(passwordResetRequest);

        verify(kafkaProperties).getPasswordResetRequestTopic();
        verify(kafkaTemplate).send(any(), any(PasswordResetRequestMessage.class));
    }

    @Test
    void givenError_WhenSendingMessage_ThenCaptureException_AndLogError() {
        PasswordResetRequest passwordResetRequest = PasswordResetRequestFixture.load();
        String logError = "[ERROR] Error when sending message to topic: [topic]. Error: [Kafka is not available]";

        when(kafkaProperties.getPasswordResetRequestTopic()).thenReturn("topic");

        Logger logger = (Logger) LoggerFactory.getLogger(PostPasswordResetRequestMessageBoundaryGatewayTest.class);
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        loggerContext.getLogger("com.iCritic.users.dataprovider.gateway.kafka.impl.PostPasswordResetRequestMessageBoundaryGateway").addAppender(listAppender);

        doThrow(new RuntimeException("Kafka is not available")).when(kafkaTemplate).send(any(), any(PasswordResetRequestMessage.class));

        passwordResetRequestGateway.execute(passwordResetRequest);

        List<ILoggingEvent> loggingEvents = listAppender.list;
        String loggedError = loggingEvents.toArray()[0].toString();
        assertEquals(logError, loggedError);
    }
}
