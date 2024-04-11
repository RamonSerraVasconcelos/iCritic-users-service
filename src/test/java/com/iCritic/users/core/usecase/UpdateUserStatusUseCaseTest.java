package com.iCritic.users.core.usecase;

import com.iCritic.users.config.properties.ApplicationProperties;
import com.iCritic.users.core.enums.BanActionEnum;
import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.DeleteUserRefreshTokensBoundary;
import com.iCritic.users.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.users.core.usecase.boundary.RemoveUserFromBlacklistBoundary;
import com.iCritic.users.core.usecase.boundary.SaveUserToBlacklistBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateBanListBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateUserStatusBoundary;
import com.iCritic.users.exception.ResourceNotFoundException;
import com.iCritic.users.exception.ResourceViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserStatusUseCaseTest {

    @InjectMocks
    private UpdateUserStatusUseCase updateUserStatusUseCase;

    @Mock
    private FindUserByIdBoundary findUserByIdBoundary;

    @Mock
    private UpdateUserStatusBoundary updateUserStatusBoundary;

    @Mock
    private UpdateBanListBoundary updateBanListBoundary;

    @Mock
    private SaveUserToBlacklistBoundary saveUserToBlacklistBoundary;

    @Mock
    private RemoveUserFromBlacklistBoundary removeUserFromBlacklistBoundary;

    @Mock
    private DeleteUserRefreshTokensBoundary deleteUserRefreshTokensBoundary;

    @Mock
    private ApplicationProperties applicationProperties;

    @Test
    void givenValidFields_thenCall_updateUserStatusBoundaryWithActiveFalse_and_updateBanListBoundaryWithActionBan() {
        User user = UserFixture.load();
        String motive = "Motive";
        BanActionEnum action = BanActionEnum.BAN;
        boolean active = false;

        when(findUserByIdBoundary.execute(user.getId())).thenReturn(user);
        doNothing().when(updateUserStatusBoundary).execute(user.getId(), active);
        doNothing().when(updateBanListBoundary).execute(user.getId(), motive, action.toString());

        updateUserStatusUseCase.execute(user.getId(), motive, action);

        verify(findUserByIdBoundary).execute(user.getId());
        verify(updateUserStatusBoundary).execute(user.getId(), active);
        verify(updateBanListBoundary).execute(user.getId(), motive, action.toString());
        verify(saveUserToBlacklistBoundary).execute(user.getId(), applicationProperties.getJwtExpiration());
        verify(deleteUserRefreshTokensBoundary).execute(user.getId());
        verifyNoInteractions(removeUserFromBlacklistBoundary);
    }

    @Test
    void givenValidFields_thenCall_updateUserStatusBoundaryWithActiveTrue_and_updateBanListBoundaryWithActionUnban() {
        User user = UserFixture.load();
        String motive = "Motive";
        BanActionEnum action = BanActionEnum.UNBAN;
        boolean active = true;

        when(findUserByIdBoundary.execute(user.getId())).thenReturn(user);
        doNothing().when(updateUserStatusBoundary).execute(user.getId(), active);
        doNothing().when(updateBanListBoundary).execute(user.getId(), motive, action.toString());

        updateUserStatusUseCase.execute(user.getId(), motive, action);

        verify(findUserByIdBoundary).execute(user.getId());
        verify(updateUserStatusBoundary).execute(user.getId(), active);
        verify(updateBanListBoundary).execute(user.getId(), motive, action.toString());
        verify(removeUserFromBlacklistBoundary).execute(user.getId());
        verifyNoInteractions(saveUserToBlacklistBoundary);
        verifyNoInteractions(deleteUserRefreshTokensBoundary);
    }

    @Test
    void givenNullId_thenThrowResourceViolationException() {
        String motive = "Motive";
        BanActionEnum action = BanActionEnum.BAN;

        assertThrows(ResourceViolationException.class, () -> updateUserStatusUseCase.execute(null, motive, action));
    }

    @Test
    void givenInvalidId_thenThrowResourceNotFoundException() {
        Long id = 1L;
        String motive = "Motive";
        BanActionEnum action = BanActionEnum.BAN;

        when(findUserByIdBoundary.execute(id)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> updateUserStatusUseCase.execute(id, motive, action));
    }
}
