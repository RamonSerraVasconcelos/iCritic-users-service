package com.iCritic.users.core.usecase;

import com.iCritic.users.core.enums.Role;
import com.iCritic.users.exception.ForbiddenAccessException;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.nonNull;

@Service
@NoArgsConstructor
public class ValidateUserRoleUseCase {

    public void execute(List<Role> allowedRoles, String currentRole) {
        if(!nonNull(currentRole)) {
            throw new ForbiddenAccessException();
        }

        if(Role.valueOf(currentRole) == Role.ADMIN) {
            return;
        }

        if(!allowedRoles.contains(Role.valueOf(currentRole))) {
            throw new ForbiddenAccessException();
        }
    }
}
