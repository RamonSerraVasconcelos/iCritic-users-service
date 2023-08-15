package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.RefreshToken;

public interface SaveRefreshTokenBoundary {

    void execute(RefreshToken refreshToken);
}
