package com.iCritic.iCritic.core.usecase.boundary;

import com.iCritic.iCritic.core.model.User;

import java.util.List;

public interface FindUsersBoundary {

    List<User> execute();
}
