package com.iCritic.users.core.usecase.boundary;

public interface PostPasswordChangeMessageBoundary {

    void execute(Long userId, String email);
}
