package com.iCritic.users.dataprovider.jwt;

import com.iCritic.users.core.model.RefreshToken;
import com.iCritic.users.core.usecase.boundary.DeleteUserRefreshTokensBoundary;
import com.iCritic.users.core.usecase.boundary.FindRefreshTokenByIdBoundary;
import com.iCritic.users.core.usecase.boundary.SaveRefreshTokenBoundary;
import com.iCritic.users.dataprovider.gateway.database.entity.UserEntity;
import com.iCritic.users.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtManager {

    private final SaveRefreshTokenBoundary saveRefreshTokenBoundary;

    private final DeleteUserRefreshTokensBoundary deleteUserRefreshTokensBoundary;

    private final FindRefreshTokenByIdBoundary findRefreshTokenByIdBoundary;

    public void saveRefreshToken(String id, UserEntity userEntity, LocalDateTime issuedAt, LocalDateTime expiresAt) {
        RefreshToken refreshToken = RefreshToken.builder()
                .id(id)
                .userEntity(userEntity)
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .active(true)
                .build();

        saveRefreshTokenBoundary.execute(refreshToken);
    }

    public boolean isTokenActive(String tokenId) {
        Optional<RefreshToken> refreshToken = findRefreshTokenByIdBoundary.execute(tokenId);

        if(refreshToken.isEmpty()) {
            throw new UnauthorizedAccessException("Invalid refresh token");
        }

        return refreshToken.get().isActive();
    }

    public void revokeUserTokens(Long userId) {
        deleteUserRefreshTokensBoundary.execute(userId);
    }
}
