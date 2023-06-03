package com.iCritic.iCritic.entrypoint.validation;

import com.iCritic.iCritic.core.enums.Role;
import com.iCritic.iCritic.exception.ForbiddenAccessException;

import java.util.List;

import static java.util.Objects.nonNull;

public class RoleValidator {

    public static void validate(List<Role> allowedRoles, String currentRole) {
        if(!nonNull(currentRole)) {
            throw new ForbiddenAccessException("insufficient access rights");
        }

        if(Role.valueOf(currentRole) == Role.ADMIN) {
            return;
        }

        if(!allowedRoles.contains(Role.valueOf(currentRole))) {
            throw new ForbiddenAccessException("insufficient access rights");
        }
    }
}
