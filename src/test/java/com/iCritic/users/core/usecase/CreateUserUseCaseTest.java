package com.iCritic.users.core.usecase;

import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.CreateUserBoundary;
import com.iCritic.users.core.usecase.boundary.FindCountryByIdBoundary;
import com.iCritic.users.core.usecase.boundary.FindUserByEmailBoundary;
import com.iCritic.users.exception.ResourceConflictException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Mock
    private CreateUserBoundary createUserBoundary;

    @Mock
    private FindUserByEmailBoundary findUserByEmailBoundary;

    @Mock
    private FindCountryByIdBoundary findCountryByIdBoundary;

    @Mock
    private BCryptPasswordEncoder bcrypt;

    @Test
    void givenValidEmailAndPassword_thenCreateAndReturnUser() {
        User user = UserFixture.load();
        String initialPassword = user.getPassword();
        String encodedPassword = "2d6h2d0shf@837fs92#123";

        when(findUserByEmailBoundary.execute(user.getEmail())).thenReturn(null);
        when(findCountryByIdBoundary.execute(user.getCountryId())).thenReturn(user.getCountry());
        when(bcrypt.encode(user.getPassword())).thenReturn(encodedPassword);
        when(createUserBoundary.execute(user)).thenReturn(user);

        User createdUser = createUserUseCase.execute(user);

        verify(findUserByEmailBoundary).execute(user.getEmail());
        verify(findCountryByIdBoundary).execute(user.getCountryId());
        verify(bcrypt).encode(initialPassword);
        verify(createUserBoundary).execute(user);

        assertNotNull(createdUser);
        assertEquals(createdUser.getName(), user.getName());
        assertEquals(createdUser.getEmail(), user.getEmail());
        assertEquals(createdUser.getDescription(), user.getDescription());
        assertEquals(createdUser.getPassword(), encodedPassword);
        assertEquals(createdUser.isActive(), user.isActive());
        assertEquals(createdUser.getRole(), user.getRole());
        assertEquals(createdUser.getCountry(), user.getCountry());
        assertEquals(createdUser.getCreatedAt(), user.getCreatedAt());
    }

    @Test
    void givenDuplicatedEmail_thenThrowResourceConflictException() {
        User user = UserFixture.load();

        when(findUserByEmailBoundary.execute(user.getEmail())).thenReturn(user);

        assertThrows(ResourceConflictException.class, () -> createUserUseCase.execute(user));
    }
}
