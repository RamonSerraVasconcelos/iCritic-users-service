package com.iCritic.users.core.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.iCritic.users.core.enums.Role;
import com.iCritic.users.exception.ForbiddenAccessException;

@ExtendWith(MockitoExtension.class)
class ValidateUserRoleUseCaseTest {

    @InjectMocks
    private ValidateUserRoleUseCase validateUserRoleUseCase;

    @Test
    void givenValidParameters_andMatchingRoles_thenAllowed() {
        List<Role> allowedRoles = List.of(Role.MODERATOR);

        String currentRole = "MODERATOR";

        assertDoesNotThrow(() -> validateUserRoleUseCase.execute(allowedRoles, currentRole));
    }

    @Test
    void givenCallWithCurrentRoleAdmin_thenAlwaysAllow() {
        List<Role> allowedRoles = List.of(Role.MODERATOR);

        String currentRole = "ADMIN";

        assertDoesNotThrow(() -> validateUserRoleUseCase.execute(allowedRoles, currentRole));
    }

    @Test
    void givenNullCurrentRole_thenThrowForbiddenAccessException() {
        List<Role> allowedRoles = List.of(Role.MODERATOR);

        assertThrows(ForbiddenAccessException.class, () -> validateUserRoleUseCase.execute(allowedRoles, null));
    }

    @Test
    void givenCallWithUnmatchedRole_thenThrowForbiddenAccessException() {
        List<Role> allowedRoles = List.of(Role.MODERATOR);

        String currentRole = "DEFAULT";

        assertThrows(ForbiddenAccessException.class, () -> validateUserRoleUseCase.execute(allowedRoles, currentRole));
    }
}
