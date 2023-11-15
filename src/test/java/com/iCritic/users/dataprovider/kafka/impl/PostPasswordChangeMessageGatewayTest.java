package com.iCritic.users.dataprovider.kafka.impl;

import com.iCritic.users.dataprovider.gateway.kafka.impl.PostPasswordChangeMessageGateway;
import com.iCritic.users.dataprovider.gateway.kafka.producer.PasswordChangeMessageProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostPasswordChangeMessageGatewayTest {

    @InjectMocks
    private PostPasswordChangeMessageGateway postPasswordChangeMessageGateway;

    @Mock
    private PasswordChangeMessageProducer producer;

    @Test
    void givenParameters_thenSendMessage () {
        postPasswordChangeMessageGateway.execute(1L, "test@test.test");

        verify(producer).execute(any());
    }
}
