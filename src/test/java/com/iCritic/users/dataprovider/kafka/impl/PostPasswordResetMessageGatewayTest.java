package com.iCritic.users.dataprovider.kafka.impl;

import com.iCritic.users.dataprovider.gateway.kafka.impl.PostPasswordResetMessageGateway;
import com.iCritic.users.dataprovider.gateway.kafka.producer.PasswordResetMessageProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostPasswordResetMessageGatewayTest {

    @InjectMocks
    private PostPasswordResetMessageGateway postPasswordResetMessageGateway;

    @Mock
    private PasswordResetMessageProducer producer;

    @Test
    void givenParameters_thenSendMessage() {
        Long userId = 1L;
        String email = "test@test.test";

        postPasswordResetMessageGateway.execute(userId, email);

        verify(producer).execute(any());
    }
}
