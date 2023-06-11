package com.iCritic.iCritic.core.usecase;

import com.iCritic.iCritic.core.fixture.UserFixture;
import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.core.usecase.boundary.FindUsersBoundary;
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
class FindUsersUseCaseTest {

    @InjectMocks
    private FindUsersUseCase findUsersUseCase;

    @Mock
    private FindUsersBoundary findUsersBoundary;

    @Test
    void givenCallToUseCase_thenReturnUsers() {
        List<User> users = List.of(
                UserFixture.load(),
                UserFixture.load(),
                UserFixture.load()
        );

        when(findUsersBoundary.execute()).thenReturn(users);

        List<User> returnedUsers = findUsersUseCase.execute();

        verify(findUsersBoundary).execute();
        assertNotNull(returnedUsers);
        assertEquals(returnedUsers.size(), users.size());
        assertEquals(returnedUsers.get(0).getName(), users.get(0).getName());
        assertEquals(returnedUsers.get(0).getEmail(), users.get(0).getEmail());
        assertEquals(returnedUsers.get(0).getDescription(), users.get(0).getDescription());
        assertEquals(returnedUsers.get(0).isActive(), users.get(0).isActive());
        assertEquals(returnedUsers.get(0).getRole(), users.get(0).getRole());
        assertEquals(returnedUsers.get(0).getCountry(), users.get(0).getCountry());
        assertEquals(returnedUsers.get(0).getCreatedAt(), users.get(0).getCreatedAt());
    }
}
