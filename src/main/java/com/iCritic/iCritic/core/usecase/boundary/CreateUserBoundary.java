package com.iCritic.iCritic.core.usecase.boundary;

import com.iCritic.iCritic.core.model.User;

public interface CreateUserBoundary {

    User execute(User user);
}
