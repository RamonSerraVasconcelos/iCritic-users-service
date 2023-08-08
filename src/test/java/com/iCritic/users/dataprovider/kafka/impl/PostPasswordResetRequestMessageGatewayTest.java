package com.iCritic.users.dataprovider.kafka.impl;

import com.iCritic.users.core.fixture.PasswordResetRequestFixture;
import com.iCritic.users.core.model.PasswordResetRequest;
import com.iCritic.users.dataprovider.gateway.kafka.entity.PasswordResetRequestMessage;
import com.iCritic.users.dataprovider.gateway.kafka.impl.PostPasswordResetRequestMessageGateway;
import com.iCritic.users.dataprovider.gateway.kafka.producer.PasswordResetRequestMessageProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostPasswordResetRequestMessageGatewayTest {

    @InjectMocks
    private PostPasswordResetRequestMessageGateway passwordResetRequestGateway;

    @Mock
    private PasswordResetRequestMessageProducer producer;

    @Captor
    private ArgumentCaptor<PasswordResetRequestMessage> passwordResetRequestMessageArgumentCaptor;

    @Test
    void given_PasswordResetRequest_when_executed_then_send_message() {
        PasswordResetRequest passwordResetRequest = PasswordResetRequestFixture.load();

        passwordResetRequestGateway.execute(passwordResetRequest);

        verify(producer).execute(passwordResetRequestMessageArgumentCaptor.capture());
        assertEquals(passwordResetRequest.getEmail(), passwordResetRequestMessageArgumentCaptor.getValue().getEmail());
        assertEquals(passwordResetRequest.getUserId(), passwordResetRequestMessageArgumentCaptor.getValue().getUserId());
        assertEquals(passwordResetRequest.getPasswordResetHash(), passwordResetRequestMessageArgumentCaptor.getValue().getPasswordResetHash());
    }
}
