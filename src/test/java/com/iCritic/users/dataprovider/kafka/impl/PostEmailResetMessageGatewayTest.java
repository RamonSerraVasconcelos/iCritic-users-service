package com.iCritic.users.dataprovider.kafka.impl;

import com.iCritic.users.core.model.EmailReset;
import com.iCritic.users.dataprovider.gateway.kafka.impl.PostEmailResetMessageGateway;
import com.iCritic.users.dataprovider.gateway.kafka.producer.EmailResetMessageProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostEmailResetMessageGatewayTest {

    @InjectMocks
    private PostEmailResetMessageGateway postEmailResetMessageGateway;

    @Mock
    private EmailResetMessageProducer producer;

    @Test
    void givenParameters_thenSendMessage() {
        EmailReset emailReset = EmailReset.builder()
                .userId(1L)
                .email("test@test.test")
                .build();

        postEmailResetMessageGateway.execute(emailReset);

        verify(producer).execute(any());
    }
}
