package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.User;

public interface CreateUserBoundary {

    User execute(User user);
}
