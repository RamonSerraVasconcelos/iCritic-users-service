package com.iCritic.users.core.usecase;

import com.iCritic.users.core.enums.Role;
import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindCountryByIdBoundary;
import com.iCritic.users.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateUserBoundary;
import com.iCritic.users.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

    @InjectMocks
    private UpdateUserUseCase updateUserUseCase;

    @Mock
    private UpdateUserBoundary updateUserBoundary;

    @Mock
    private FindUserByIdBoundary findUserByIdBoundary;

    @Mock
    private FindCountryByIdBoundary findCountryByIdBoundary;

    @Captor
    ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void givenValidUser_thenCallUpdateUserBoundary_andReturnUpdatedUser() {
        User user = UserFixture.load();
        User foundUser = UserFixture.load();

        user.setName("new name");
        user.setDescription("new description");

        when(findUserByIdBoundary.execute(user.getId())).thenReturn(foundUser);
        when(findCountryByIdBoundary.execute(user.getCountryId())).thenReturn(foundUser.getCountry());
        when(updateUserBoundary.execute(any())).thenReturn(user);

        User updatedUser = updateUserUseCase.execute(user.getId(), user);

        verify(findUserByIdBoundary).execute(foundUser.getId());
        verify(findCountryByIdBoundary).execute(foundUser.getCountryId());
        verify(updateUserBoundary).execute(any());

        assertNotNull(updatedUser);
        assertEquals(updatedUser.getName(), user.getName());
        assertEquals(updatedUser.getEmail(), user.getEmail());
        assertEquals(updatedUser.getDescription(), user.getDescription());
        assertEquals(updatedUser.isActive(), user.isActive());
        assertEquals(updatedUser.getRole(), user.getRole());
        assertEquals(updatedUser.getCountry(), user.getCountry());
        assertEquals(updatedUser.getCreatedAt(), user.getCreatedAt());
    }

    @Test
    void givenValidUser_whenExecuteUseCase_dontCallBoundaryWithUnauthorizedFields() {
        User user = UserFixture.load();
        User foundUser = UserFixture.load();

        user.setName("new name");
        user.setDescription("new description");

        user.setEmail("shouldnotupdateemail@test.test");
        user.setPassword("should not update password");
        user.setActive(false);
        user.setRole(Role.ADMIN);
        user.setCreatedAt(LocalDateTime.now().plusDays(100));

        when(findUserByIdBoundary.execute(user.getId())).thenReturn(foundUser);
        when(findCountryByIdBoundary.execute(user.getCountryId())).thenReturn(foundUser.getCountry());
        when(updateUserBoundary.execute(userArgumentCaptor.capture())).thenReturn(any());

        updateUserUseCase.execute(user.getId(), user);

        User userToBeUpdated = userArgumentCaptor.getValue();

        assertNotEquals(userToBeUpdated.getEmail(), user.getEmail());
        assertNotEquals(userToBeUpdated.getPassword(), user.getPassword());
        assertNotEquals(userToBeUpdated.isActive(), user.isActive());
        assertNotEquals(userToBeUpdated.getRole(), user.getRole());
        assertNotEquals(userToBeUpdated.getCreatedAt(), user.getCreatedAt());
    }

    @Test
    void givenInvalidId_thenThrowResourceNotFoundException() {
        User user = UserFixture.load();

        when(findUserByIdBoundary.execute(user.getId())).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> updateUserUseCase.execute(user.getId(), user));
    }

    @Test
    void givenInvalidCountryId_thenThrowResourceNotFoundException() {
        User user = UserFixture.load();

        when(findUserByIdBoundary.execute(user.getId())).thenReturn(user);
        when(findCountryByIdBoundary.execute(user.getCountryId())).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> updateUserUseCase.execute(user.getId(), user));
    }
}
