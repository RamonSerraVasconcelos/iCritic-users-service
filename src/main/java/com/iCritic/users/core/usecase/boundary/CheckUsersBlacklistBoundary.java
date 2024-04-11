package com.iCritic.users.core.usecase.boundary;

public interface CheckUsersBlacklistBoundary {

    boolean isUserBlackListed(Long userId);
}
