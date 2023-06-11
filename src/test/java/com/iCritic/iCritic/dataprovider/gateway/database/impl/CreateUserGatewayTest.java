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
class CreateUserGatewayTest {

    @InjectMocks
    private CreateUserGateway createUserGateway;

    @Mock
    private UserRepository userRepository;

    @Test
    void givenExecution_callUserRepository_andReturnUser() {
        User user = UserFixture.load();
        UserEntity userEntity = UserEntityFixture.load();

        when(userRepository.save(any())).thenReturn(userEntity);

        User createdUser = createUserGateway.execute(user);

        verify(userRepository).save(any());
        assertNotNull(createdUser);
        assertEquals(createdUser.getName(), user.getName());
        assertEquals(createdUser.getEmail(), user.getEmail());
        assertEquals(createdUser.getDescription(), user.getDescription());
        assertEquals(createdUser.getPassword(), user.getPassword());
        assertEquals(createdUser.isActive(), user.isActive());
        assertEquals(createdUser.getRole(), user.getRole());
        assertEquals(createdUser.getCountry().getId(), user.getCountry().getId());
        assertEquals(createdUser.getCountry().getName(), user.getCountry().getName());
        assertEquals(createdUser.getCreatedAt(), userEntity.getCreatedAt());
    }
}
