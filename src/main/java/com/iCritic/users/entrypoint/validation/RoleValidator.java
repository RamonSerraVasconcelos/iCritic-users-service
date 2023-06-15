package com.iCritic.users.entrypoint.validation;

import com.iCritic.users.core.enums.Role;
import com.iCritic.users.exception.ForbiddenAccessException;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Objects.nonNull;

@Component
@NoArgsConstructor
public class RoleValidator {

    public void validate(List<Role> allowedRoles, String currentRole) {
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
