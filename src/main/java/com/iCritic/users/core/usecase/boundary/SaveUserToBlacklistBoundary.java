package com.iCritic.users.core.usecase.boundary;

import java.time.LocalDateTime;

public interface SaveUserToBlacklistBoundary {

    void execute(Long userId, int expirationDateTimeInSec);
}
