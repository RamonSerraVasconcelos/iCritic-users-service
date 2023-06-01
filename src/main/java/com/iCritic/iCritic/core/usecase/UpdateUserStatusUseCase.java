package com.iCritic.iCritic.core.usecase;

import com.iCritic.iCritic.core.enums.BanActionEnum;
import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.iCritic.core.usecase.boundary.UpdateBanListBoundary;
import com.iCritic.iCritic.core.usecase.boundary.UpdateUserStatusBoundary;
import com.iCritic.iCritic.entrypoint.model.UserBanDto;
import com.iCritic.iCritic.exception.ResourceNotFoundException;
import com.iCritic.iCritic.exception.ResourceViolationException;
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

    public void execute(Long id, UserBanDto userBanDto, BanActionEnum action) {
        if (!nonNull(id)) {
            throw new ResourceViolationException("Invalid id");
        }

        User user = findUserByIdBoundary.execute(id);

        if (!nonNull(user)) {
            throw new ResourceNotFoundException("User not found");
        }

        boolean active = action != BanActionEnum.BAN;

        updateUserStatusBoundary.execute(id, active);
        updateBanListBoundary.execute(id, userBanDto.getMotive(), action.toString());
    }
}
