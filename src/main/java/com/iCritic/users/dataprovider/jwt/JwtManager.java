package com.iCritic.users.dataprovider.jwt;

import com.iCritic.users.core.model.User;
import com.iCritic.users.dataprovider.gateway.database.entity.RefreshTokenEntity;
import com.iCritic.users.dataprovider.gateway.database.entity.UserEntity;
import com.iCritic.users.dataprovider.gateway.database.repository.RefreshTokenRepository;
import com.iCritic.users.exception.ResourceNotFoundException;
import com.iCritic.users.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtManager {

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(String id, UserEntity userEntity, LocalDateTime issuedAt, LocalDateTime expiresAt) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .id(id)
                .userEntity(userEntity)
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .active(true)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
    }


    public boolean isTokenActive(String tokenId) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findById(tokenId).orElseThrow(() -> new UnauthorizedAccessException("Invalid refresh token"));

        return refreshTokenEntity.isActive();
    }

    public void revokeUserTokens(Long userId) {
        refreshTokenRepository.revokeUserTokens(userId);
    }
}
