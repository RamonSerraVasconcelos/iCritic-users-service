package com.iCritic.iCritic.dataprovider.gateway.database.impl;

import com.iCritic.iCritic.core.fixture.UserFixture;
import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.dataprovider.gateway.database.entity.UserEntity;
import com.iCritic.iCritic.dataprovider.gateway.database.fixture.UserEntityFixture;
import com.iCritic.iCritic.dataprovider.gateway.database.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserGatewayTest {

    @InjectMocks
    private UpdateUserGateway updateUserGateway;

    @Mock
    private UserRepository userRepository;

    @Test
    void givenExecution_callUserRepository_andReturnUpdatedUser() {
        User user = UserFixture.load();
        UserEntity userEntity = UserEntityFixture.load();

        when(userRepository.save(any())).thenReturn(userEntity);

        User updatedUser = updateUserGateway.execute(user);

        verify(userRepository).save(any());
        assertNotNull(updatedUser);
        assertEquals(updatedUser.getName(), user.getName());
        assertEquals(updatedUser.getEmail(), user.getEmail());
        assertEquals(updatedUser.getDescription(), user.getDescription());
        assertEquals(updatedUser.isActive(), user.isActive());
        assertEquals(updatedUser.getRole(), user.getRole());
        assertEquals(updatedUser.getCountry().getId(), user.getCountry().getId());
        assertEquals(updatedUser.getCountry().getName(), user.getCountry().getName());
        assertNotNull(updatedUser.getCreatedAt());
    }
}
