package com.iCritic.users.core.usecase;

import com.iCritic.users.core.enums.BanActionEnum;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateBanListBoundary;
import com.iCritic.users.core.usecase.boundary.UpdateUserStatusBoundary;
import com.iCritic.users.exception.ResourceNotFoundException;
import com.iCritic.users.exception.ResourceViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateUserStatusUseCase {
    private final FindUserByIdBoundary findUserByIdBoundary;

    private final UpdateUserStatusBoundary updateUserStatusBoundary;

    private final UpdateBanListBoundary updateBanListBoundary;

    public void execute(Long id, String motive, BanActionEnum action) {
        if (!nonNull(id)) {
            throw new ResourceViolationException("Invalid id");
        }

        User user = findUserByIdBoundary.execute(id);

        if (!nonNull(user)) {
            throw new ResourceNotFoundException("User not found");
        }

        boolean active = action != BanActionEnum.BAN;

        updateUserStatusBoundary.execute(id, active);
        updateBanListBoundary.execute(id, motive, action.toString());
    }
}
