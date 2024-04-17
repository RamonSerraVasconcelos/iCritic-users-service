package com.iCritic.users.core.usecase;

import com.iCritic.users.core.model.Claim;
import com.iCritic.users.core.model.RefreshToken;
import com.iCritic.users.core.usecase.boundary.DeleteUserRefreshTokensBoundary;
import com.iCritic.users.core.usecase.boundary.FindRefreshTokenByIdBoundary;
import com.iCritic.users.core.usecase.boundary.ValidateRefreshTokenBoundary;
import com.iCritic.users.core.utils.TokenUtils;
import com.iCritic.users.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidateRefreshTokenUseCase {

    private final ValidateRefreshTokenBoundary validateRefreshTokenBoundary;

    private final FindRefreshTokenByIdBoundary findRefreshTokenByIdBoundary;

    private final DeleteUserRefreshTokensBoundary deleteUserRefreshTokensBoundary;

    public RefreshToken execute(String encodedToken) {
        RefreshToken refreshToken = validateRefreshTokenBoundary.execute(encodedToken);

        if (isNull(refreshToken) || isNull(refreshToken.getId())) {
            throw new UnauthorizedAccessException("Invalid refresh token");
        }

        log.info("Validating refresh token with id [{}]", refreshToken.getId());

        Optional<RefreshToken> savedToken = findRefreshTokenByIdBoundary.execute(refreshToken.getId());

        if (savedToken.isEmpty()) {
            throw new UnauthorizedAccessException("Invalid refresh token");
        }

        if (!savedToken.get().isActive()) {
            Claim userIdClaim = TokenUtils.getClaim(refreshToken.getClaims(), "userId");

            if(isNull(userIdClaim)) {
                throw new UnauthorizedAccessException("Invalid refresh token");
            }

            Long userId = Long.parseLong(userIdClaim.getValue());

            deleteUserRefreshTokensBoundary.execute(userId);

            log.error("Failed validating refresh token with id: [{}]", refreshToken.getId());
            throw new UnauthorizedAccessException("Invalid refresh token");
        }

        return refreshToken;
    }
}
