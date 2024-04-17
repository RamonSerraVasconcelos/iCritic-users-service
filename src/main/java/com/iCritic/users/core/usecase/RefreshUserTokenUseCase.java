package com.iCritic.users.core.usecase;

import com.iCritic.users.core.model.AccessToken;
import com.iCritic.users.core.model.AuthorizationData;
import com.iCritic.users.core.model.Claim;
import com.iCritic.users.core.model.RefreshToken;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.DeleteUserRefreshTokensBoundary;
import com.iCritic.users.core.usecase.boundary.FindUserByIdBoundary;
import com.iCritic.users.core.utils.TokenUtils;
import com.iCritic.users.exception.ResourceNotFoundException;
import com.iCritic.users.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshUserTokenUseCase {

    private final FindUserByIdBoundary findUserByIdBoundary;

    private final ValidateRefreshTokenUseCase validateRefreshTokenUseCase;

    private final DeleteUserRefreshTokensBoundary deleteUserRefreshTokensBoundary;

    private final GenerateAccessTokenUseCase generateAccessTokenUseCase;

    private final GenerateRefreshTokenUseCase generateRefreshTokenUseCase;

    public AuthorizationData execute(String encodedToken) {

        try {
            RefreshToken refreshToken = validateRefreshTokenUseCase.execute(encodedToken);

            Claim userIdClaim = TokenUtils.getClaim(refreshToken.getClaims(), "userId");

            if(isNull(userIdClaim)) {
                throw new UnauthorizedAccessException("Invalid refresh token");
            }

            Long userId = Long.parseLong(userIdClaim.getValue());

            User user = findUserByIdBoundary.execute(userId);

            log.info("Refreshing token for user with id: [{}]", user.getId());

            deleteUserRefreshTokensBoundary.execute(user.getId());

            AccessToken accessToken = generateAccessTokenUseCase.execute(user);
            RefreshToken newRefreshtoken = generateRefreshTokenUseCase.execute(user);

            return AuthorizationData.builder()
                    .accessToken(accessToken.getEncodedToken())
                    .refreshToken(newRefreshtoken.getEncodedToken())
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new UnauthorizedAccessException("Invalid refresh token");
        }
    }
}
