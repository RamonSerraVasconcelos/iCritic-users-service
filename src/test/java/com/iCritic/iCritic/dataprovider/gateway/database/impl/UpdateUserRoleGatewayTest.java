package com.iCritic.iCritic.dataprovider.gateway.database.impl;

import com.iCritic.iCritic.core.enums.Role;
import com.iCritic.iCritic.dataprovider.gateway.database.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateUserRoleGatewayTest {

    @InjectMocks
    private UpdateUserRoleGateway updateUserRoleGateway;

    @Mock
    private UserRepository userRepository;

    @Test
    void givenExecution_callUserRepository() {
        Long id = 1L;
        Role role = Role.MODERATOR;

        doNothing().when(userRepository).updateRole(id, role);

        updateUserRoleGateway.execute(id, role);

        verify(userRepository).updateRole(id, role);
    }
}
