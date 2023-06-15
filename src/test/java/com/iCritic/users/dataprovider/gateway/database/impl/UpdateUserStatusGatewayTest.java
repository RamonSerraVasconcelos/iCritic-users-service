package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.dataprovider.gateway.database.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateUserStatusGatewayTest {

    @InjectMocks
    private UpdateUserStatusGateway updateUserStatusGateway;

    @Mock
    private UserRepository userRepository;

    @Test
    void givenExecution_callUserRepository() {
        Long id = 1L;
        boolean active = false;

        doNothing().when(userRepository).updateStatus(id, active);

        updateUserStatusGateway.execute(id, active);

        verify(userRepository).updateStatus(id, active);
    }
}
