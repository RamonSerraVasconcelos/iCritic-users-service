package com.iCritic.users.dataprovider.gateway.database.impl;

import com.iCritic.users.core.model.RefreshToken;
import com.iCritic.users.core.usecase.boundary.FindRefreshTokenByIdBoundary;
import com.iCritic.users.dataprovider.gateway.database.entity.RefreshTokenEntity;
import com.iCritic.users.dataprovider.gateway.database.mapper.RefreshTokenEntityMapper;
import com.iCritic.users.dataprovider.gateway.database.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FindRefreshTokenByIdGateway implements FindRefreshTokenByIdBoundary {

    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> execute(String id) {
        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findById(id);

        RefreshTokenEntityMapper refreshTokenEntityMapper = RefreshTokenEntityMapper.INSTANCE;

        return refreshTokenEntity.map(refreshTokenEntityMapper::refreshTokenEntityToRefreshToken);
    }
}
