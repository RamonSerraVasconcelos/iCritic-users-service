package com.iCritic.users.entrypoint.validation;

import com.iCritic.users.core.enums.Role;
import com.iCritic.users.exception.ForbiddenAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class RoleValidatorTest {

    @InjectMocks
    private RoleValidator roleValidator;

    @Test
    void givenValidParameters_andMatchingRoles_thenAllowed() {
        List<Role> allowedRoles = List.of(Role.MODERATOR);

        String currentRole = "MODERATOR";

        assertDoesNotThrow(() -> roleValidator.validate(allowedRoles, currentRole));
    }

    @Test
    void givenCallWithCurrentRoleAdmin_thenAlwaysAllow() {
        List<Role> allowedRoles = List.of(Role.MODERATOR);

        String currentRole = "ADMIN";

        assertDoesNotThrow(() -> roleValidator.validate(allowedRoles, currentRole));
    }

    @Test
    void givenNullCurrentRole_thenThrowForbiddenAccessException() {
        List<Role> allowedRoles = List.of(Role.MODERATOR);

        assertThrows(ForbiddenAccessException.class, () -> roleValidator.validate(allowedRoles, null));
    }

    @Test
    void givenCallWithUnmatchedRole_thenThrowForbiddenAccessException() {
        List<Role> allowedRoles = List.of(Role.MODERATOR);

        String currentRole = "DEFAULT";

        assertThrows(ForbiddenAccessException.class, () -> roleValidator.validate(allowedRoles, currentRole));
    }
}
