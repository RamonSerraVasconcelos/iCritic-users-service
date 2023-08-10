package com.iCritic.users.core.usecase.boundary;

public interface PostPasswordResetMessageBoundary {

    void execute(Long userId, String email);
}