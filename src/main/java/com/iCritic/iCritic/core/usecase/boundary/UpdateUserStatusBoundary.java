package com.iCritic.iCritic.core.usecase.boundary;

public interface UpdateUserStatusBoundary {

    void execute(Long userId, boolean active);
}
