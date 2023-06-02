package com.iCritic.iCritic.core.usecase.boundary;

import com.iCritic.iCritic.core.model.User;

public interface FindUserByEmailBoundary {

    User execute(String email);
}
