package com.iCritic.iCritic.core.usecase;

import com.iCritic.iCritic.core.enums.Role;
import com.iCritic.iCritic.core.fixture.UserFixture;
import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.iCritic.core.usecase.boundary.UpdateUserRoleBoundary;
import com.iCritic.iCritic.exception.ResourceNotFoundException;
import com.iCritic.iCritic.exception.ResourceViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserRoleUseCaseTest {

    @InjectMocks
    private UpdateUserRoleUseCase updateUserRoleUseCase;

    @Mock
    private UpdateUserRoleBoundary updateUserRoleBoundary;

    @Mock
    private FindUserByIdBoundary findUserByIdBoundary;

    @Test
    void givenValidIdAndRole_thenUpdateUserRole() {
        User user = UserFixture.load();

        when(findUserByIdBoundary.execute(user.getId())).thenReturn(user);
        doNothing().when(updateUserRoleBoundary).execute(user.getId(), user.getRole());

        updateUserRoleUseCase.execute(user.getId(), user.getRole().name());

        verify(findUserByIdBoundary).execute(user.getId());
        verify(updateUserRoleBoundary).execute(user.getId(), Role.valueOf(user.getRole().name()));
    }

    @Test
    void givenNullId_thenThrowResourceViolationException() {
        assertThrows(ResourceViolationException.class, () -> updateUserRoleUseCase.execute(null, Role.MODERATOR.name()));
    }

    @Test
    void givenNullRole_thenThrowResourceViolationException() {
        Long id = 1L;

        assertThrows(ResourceViolationException.class, () -> updateUserRoleUseCase.execute(id, null));
    }

    @Test
    void givenInvalidId_thenThrowResourceNotFoundException() {
        Long id = 1L;
        String role = "MODERATOR";

        when(findUserByIdBoundary.execute(id)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> updateUserRoleUseCase.execute(id, role));
    }

    @Test
    void givenInvalidRole_thenThrowResourceViolationException() {
        User user = UserFixture.load();
        String invalidRole = "INVALID_ROLE";

        when(findUserByIdBoundary.execute(user.getId())).thenReturn(user);

        assertThrows(ResourceViolationException.class, () -> updateUserRoleUseCase.execute(user.getId(), invalidRole));
    }
}
