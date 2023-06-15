package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.User;

import java.util.List;

public interface FindUsersBoundary {

    List<User> execute();
}
