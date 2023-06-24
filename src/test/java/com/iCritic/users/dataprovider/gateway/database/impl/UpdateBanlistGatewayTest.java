package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.dataprovider.gateway.database.repository.UserBanListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateBanlistGatewayTest {

    @InjectMocks
    private UpdateBanlistGateway updateBanlistGateway;

    @Mock
    private UserBanListRepository userBanListRepository;

    @Test
    void givenExecution_callUserBanListRepository() {
        Long userId = 1L;
        String motive = "motive";
        String action = "BAN";

        doNothing().when(userBanListRepository).updateBanList(userId, motive, action);

        updateBanlistGateway.execute(userId, motive, action);

        verify(userBanListRepository).updateBanList(userId, motive, action);
    }
}
