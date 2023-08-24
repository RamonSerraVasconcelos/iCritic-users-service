package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.model.RefreshToken;

import java.util.Optional;

public interface FindRefreshTokenByIdBoundary {

    Optional<RefreshToken> execute(String id);
}
