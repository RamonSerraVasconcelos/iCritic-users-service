package com.iCritic.users.dataprovider.gateway.database.mapper;

import com.iCritic.users.core.model.RefreshToken;
import com.iCritic.users.dataprovider.gateway.database.entity.RefreshTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class RefreshTokenEntityMapper {

    public static final RefreshTokenEntityMapper INSTANCE = Mappers.getMapper(RefreshTokenEntityMapper.class);

    public abstract RefreshTokenEntity refreshTokenToRefreshTokenEntity(RefreshToken refreshToken);

    public abstract RefreshToken refreshTokenEntityToRefreshToken(RefreshTokenEntity refreshTokenEntity);
}
