package com.iCritic.users.core.usecase.boundary;

public interface UpdateUserStatusBoundary {

    void execute(Long userId, boolean active);
}
