package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.User;

public interface FindUserByEmailBoundary {

    User execute(String email);
}
