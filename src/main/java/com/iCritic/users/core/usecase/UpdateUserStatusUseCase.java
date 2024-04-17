package com.iCritic.users.core.usecase;

import com.iCritic.users.config.properties.ApplicationProperties;
import com.iCritic.users.core.enums.BanActionEnum;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.DeleteUserRefreshTokensBoundary;
import com.iCritic.users.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.users.core.usecase.boundary.RemoveUserFromBlacklistBoundary;
import com.iCritic.users.core.usecase.boundary.SaveUserToBlacklistBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateBanListBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateUserStatusBoundary;
import com.iCritic.users.exception.ResourceNotFoundException;
import com.iCritic.users.exception.ResourceViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateUserStatusUseCase {
    private final FindUserByIdBoundary findUserByIdBoundary;

    private final UpdateUserStatusBoundary updateUserStatusBoundary;

    private final UpdateBanListBoundary updateBanListBoundary;

    private final SaveUserToBlacklistBoundary saveUserToBlacklistBoundary;

    private final RemoveUserFromBlacklistBoundary removeUserFromBlacklistBoundary;

    private final DeleteUserRefreshTokensBoundary deleteUserRefreshTokensBoundary;

    private final ApplicationProperties applicationProperties;

    public void execute(Long id, String motive, BanActionEnum action) {
        if (!nonNull(id)) {
            throw new ResourceViolationException("Invalid id");
        }

        log.info("Updating user [{}] status with action: [{}]", id, action.name());

        User user = findUserByIdBoundary.execute(id);

        if (!nonNull(user)) {
            throw new ResourceNotFoundException("User not found");
        }

        boolean active = action != BanActionEnum.BAN;

        updateUserStatusBoundary.execute(id, active);
        updateBanListBoundary.execute(id, motive, action.toString());

        if (action == BanActionEnum.BAN) {
            deleteUserRefreshTokensBoundary.execute(user.getId());
            saveUserToBlacklistBoundary.execute(user.getId(), applicationProperties.getJwtExpiration());
        } else {
            removeUserFromBlacklistBoundary.execute(id);
        }
    }
}
