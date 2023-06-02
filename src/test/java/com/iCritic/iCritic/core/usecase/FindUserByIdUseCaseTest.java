package com.iCritic.iCritic.core.usecase;

import com.iCritic.iCritic.core.fixture.UserFixture;
import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.core.usecase.boundary.FindUserByIdBoundary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserByIdUseCaseTest {

    @InjectMocks
    private FindUserByIdUseCase findUserByIdUseCase;

    @Mock
    private FindUserByIdBoundary findUserByIdBoundary;

    @Test
    void givenValidId_ThenCallGatewayAndReturnUser() {
        User user = UserFixture.load();

        when(findUserByIdBoundary.execute(user.getId())).thenReturn(user);

        User returnedUser = findUserByIdUseCase.execute(user.getId());

        verify(findUserByIdBoundary).execute(user.getId());
        assertNotNull(returnedUser);
        assertEquals(returnedUser.getName(), user.getName());
        assertEquals(returnedUser.getEmail(), user.getEmail());
        assertEquals(returnedUser.getDescription(), user.getDescription());
        assertEquals(returnedUser.isActive(), user.isActive());
        assertEquals(returnedUser.getRole(), user.getRole());
        assertEquals(returnedUser.getCountry(), user.getCountry());
        assertEquals(returnedUser.getCreatedAt(), user.getCreatedAt());
    }
}
