package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.dataprovider.gateway.database.entity.UserEntity;
import com.iCritic.users.dataprovider.gateway.database.fixture.UserEntityFixture;
import com.iCritic.users.dataprovider.gateway.database.repository.UserRepository;
import com.iCritic.users.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserByIdGatewayTest {

    @InjectMocks
    private FindUserByIdGateway findUserByIdGateway;

    @Mock
    private UserRepository userRepository;

    @Test
    void givenExecution_callUserRepository_thenReturnUser() {
        User user = UserFixture.load();
        UserEntity userEntity = UserEntityFixture.load();

        when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));

        User foundUser = findUserByIdGateway.execute(user.getId());

        verify(userRepository).findById(any());
        assertNotNull(foundUser);
        assertEquals(foundUser.getId(), user.getId());
        assertEquals(foundUser.getName(), user.getName());
        assertEquals(foundUser.getEmail(), user.getEmail());
        assertEquals(foundUser.getDescription(), user.getDescription());
        assertEquals(foundUser.getPassword(), user.getPassword());
        assertEquals(foundUser.isActive(), user.isActive());
        assertEquals(foundUser.getRole(), user.getRole());
        assertEquals(foundUser.getCountry().getId(), user.getCountry().getId());
        assertEquals(foundUser.getCountry().getName(), user.getCountry().getName());
        assertEquals(foundUser.getCreatedAt(), userEntity.getCreatedAt());
    }

    @Test
    void givenExecution_whenUserIsNotFound_thenThrowResourceNotFoundException() {
        User user = UserFixture.load();

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> findUserByIdGateway.execute(user.getId()));
    }
}
