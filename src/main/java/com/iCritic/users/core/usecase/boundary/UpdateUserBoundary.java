package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.User;

public interface UpdateUserBoundary {

    User execute(User user);
}
