package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.dataprovider.gateway.database.entity.UserEntity;
import com.iCritic.users.dataprovider.gateway.database.fixture.UserEntityFixture;
import com.iCritic.users.dataprovider.gateway.database.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    @Mock
    private Pageable pageable;

    @Mock
    private Page<UserEntity> pageableUser;

    @Test
    void givenExecution_callUserRepository_andReturnUsersList() {
        List<User> users = List.of(UserFixture.load(), UserFixture.load(), UserFixture.load());
        List<UserEntity> usersEntity = List.of(UserEntityFixture.load(), UserEntityFixture.load(), UserEntityFixture.load());

        when(userRepository.findAllByOrderByIdAsc(pageable)).thenReturn(pageableUser);

        Page<User> foundUsers = findUsersGateway.execute(pageable);

        verify(userRepository).findAllByOrderByIdAsc(pageable);
        assertNotNull(foundUsers);
    }
}
