package com.iCritic.users.core.usecase.boundary;

public interface UpdateBanListBoundary {

    void execute(Long userId, String motive, String action);
}
