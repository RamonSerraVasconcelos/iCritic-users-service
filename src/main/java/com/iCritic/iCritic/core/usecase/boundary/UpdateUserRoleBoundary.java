package com.iCritic.iCritic.core.usecase.boundary;

import com.iCritic.iCritic.core.enums.Role;

public interface UpdateUserRoleBoundary {

    void execute(Long id, Role role);
}
