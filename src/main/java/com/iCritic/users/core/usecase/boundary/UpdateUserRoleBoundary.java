package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.enums.Role;

public interface UpdateUserRoleBoundary {

    void execute(Long id, Role role);
}
