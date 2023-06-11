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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUsersGatewayTest {

    @InjectMocks
    private FindUsersGateway findUsersGateway;

    @Mock
    private UserRepository userRepository;

    @Test
    void givenExecution_callUserRepository_andReturnUsersList() {
        List<User> users = List.of(UserFixture.load(), UserFixture.load(), UserFixture.load());
        List<UserEntity> usersEntity = List.of(UserEntityFixture.load(), UserEntityFixture.load(), UserEntityFixture.load());

        when(userRepository.findAllByOrderByIdAsc()).thenReturn(usersEntity);

        List<User> foundUsers = findUsersGateway.execute();

        verify(userRepository).findAllByOrderByIdAsc();
        assertNotNull(foundUsers);
        assertEquals(users.size(), foundUsers.size());
        assertEquals(foundUsers.get(0).getName(), users.get(0).getName());
        assertEquals(foundUsers.get(0).getEmail(), users.get(0).getEmail());
        assertEquals(foundUsers.get(0).getDescription(), users.get(0).getDescription());
        assertEquals(foundUsers.get(0).isActive(), users.get(0).isActive());
        assertEquals(foundUsers.get(0).getRole(), users.get(0).getRole());
        assertEquals(foundUsers.get(0).getCountry().getId(), users.get(0).getCountry().getId());
        assertEquals(foundUsers.get(0).getCountry().getName(), users.get(0).getCountry().getName());
        assertNotNull(foundUsers.get(0).getCreatedAt());
    }
}
