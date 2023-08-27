package com.iCritic.users.dataprovider.kafka.impl;

import com.iCritic.users.core.fixture.EmailResetRequestFixture;
import com.iCritic.users.core.model.EmailResetRequest;
import com.iCritic.users.dataprovider.gateway.kafka.entity.EmailResetRequestMessage;
import com.iCritic.users.dataprovider.gateway.kafka.impl.PostEmailResetRequestMessageGateway;
import com.iCritic.users.dataprovider.gateway.kafka.producer.EmailResetRequestMessageProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostEmailResetRequestMessageGatewayTest {

    @InjectMocks
    private PostEmailResetRequestMessageGateway postEmailResetRequestMessageGateway;

    @Mock
    private EmailResetRequestMessageProducer producer;

    @Test
    void givenParameters_thenSendMessage() {
        EmailResetRequest emailResetRequest = EmailResetRequestFixture.load();

        postEmailResetRequestMessageGateway.execute(emailResetRequest);

        verify(producer).execute(any(EmailResetRequestMessage.class));
    }
}
