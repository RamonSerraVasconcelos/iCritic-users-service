package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.core.usecase.boundary.DeleteUserRefreshTokensBoundary;
import com.iCritic.users.dataprovider.gateway.database.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserRefreshTokensGateway implements DeleteUserRefreshTokensBoundary {

    private final RefreshTokenRepository refreshTokenRepository;

    public void execute(Long userId) {
        refreshTokenRepository.revokeUserTokens(userId);
    }
}
