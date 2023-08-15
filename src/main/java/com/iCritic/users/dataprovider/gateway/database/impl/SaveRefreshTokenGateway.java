package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.core.model.RefreshToken;
import com.iCritic.users.core.usecase.boundary.SaveRefreshTokenBoundary;
import com.iCritic.users.dataprovider.gateway.database.entity.RefreshTokenEntity;
import com.iCritic.users.dataprovider.gateway.database.mapper.RefreshTokenEntityMapper;
import com.iCritic.users.dataprovider.gateway.database.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveRefreshTokenGateway implements SaveRefreshTokenBoundary {

    private final RefreshTokenRepository refreshTokenRepository;

    public void execute(RefreshToken refreshToken) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntityMapper.INSTANCE.refreshTokenToRefreshTokenEntity(refreshToken);

        refreshTokenRepository.save(refreshTokenEntity);
    }
}
