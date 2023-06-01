package com.iCritic.iCritic.core.usecase;

import com.iCritic.iCritic.core.enums.Role;
import com.iCritic.iCritic.core.model.User;
import com.iCritic.iCritic.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.iCritic.core.usecase.boundary.UpdateUserRoleBoundary;
import com.iCritic.iCritic.exception.ResourceNotFoundException;
import com.iCritic.iCritic.exception.ResourceViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateUserRoleUseCase {

    private final UpdateUserRoleBoundary updateUserRoleBoundary;

    private final FindUserByIdBoundary findUserByIdBoundary;

    public void execute(Long id, String role) {
        if(!nonNull(id)) {
            throw new ResourceViolationException("Invalid id");
        }

        if(!nonNull(role)) {
            throw new ResourceViolationException("Invalid role");
        }

        User user = findUserByIdBoundary.execute(id);

        if(!nonNull(user)) {
            throw new ResourceNotFoundException("User not found");
        }

        try {
            updateUserRoleBoundary.execute(id, Role.valueOf(role));
        } catch (IllegalArgumentException ex) {
            throw new ResourceViolationException("Invalid role");
        }
    }
}
