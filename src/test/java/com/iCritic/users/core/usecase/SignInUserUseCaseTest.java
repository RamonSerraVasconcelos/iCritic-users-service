package com.iCritic.users.core.usecase;

import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUserByEmailBoundary;
import com.iCritic.users.exception.ResourceViolationException;
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
class SignInUserUseCaseTest {

    @InjectMocks
    private SignInUserUseCase signInUserUseCase;

    @Mock
    private FindUserByEmailBoundary findUserByEmailBoundary;

    @Mock
    private BCryptPasswordEncoder bcrypt;

    @Test
    void givenValidEmailAndPassword_thenReturnTrue() {
        User user = UserFixture.load();
        User foundUser = UserFixture.load();

        when(findUserByEmailBoundary.execute(user.getEmail())).thenReturn(foundUser);
        when(bcrypt.matches(user.getPassword(), foundUser.getPassword())).thenReturn(true);

        User loggedUser = signInUserUseCase.execute(user);

        verify(findUserByEmailBoundary).execute(user.getEmail());
        verify(bcrypt).matches(user.getPassword(), foundUser.getPassword());

        assertNotNull(loggedUser);
    }

    @Test
    void givenInvalidEmail_thenThrowResourceViolationException() {
        User user = UserFixture.load();

        when(findUserByEmailBoundary.execute(user.getEmail())).thenReturn(null);

        assertThrows(ResourceViolationException.class, () -> signInUserUseCase.execute(user));
    }

    @Test
    void givenInvalidPassword_thenThrowResourceViolationException() {
        User user = UserFixture.load();
        User foundUser = UserFixture.load();

        when(findUserByEmailBoundary.execute(user.getEmail())).thenReturn(foundUser);
        when(bcrypt.matches(user.getPassword(), foundUser.getPassword())).thenReturn(false);

        assertThrows(ResourceViolationException.class, () -> signInUserUseCase.execute(user));
    }
}
