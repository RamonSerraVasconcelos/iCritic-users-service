package com.iCritic.users.core.usecase;

import com.iCritic.users.core.model.AccessToken;
import com.iCritic.users.core.model.AuthorizationData;
import com.iCritic.users.core.model.RefreshToken;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.DeleteUserRefreshTokensBoundary;
import com.iCritic.users.core.usecase.boundary.FindUserByEmailBoundary;
import com.iCritic.users.exception.ResourceViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
@Slf4j
public class SignInUserUseCase {

    private final FindUserByEmailBoundary findUserByEmailBoundary;

    private final GenerateAccessTokenUseCase generateAccessTokenUseCase;

    private final GenerateRefreshTokenUseCase generateRefreshTokenUseCase;

    private final DeleteUserRefreshTokensBoundary deleteUserRefreshTokensBoundary;

    private final BCryptPasswordEncoder bcrypt;

    public AuthorizationData execute(User userData) {
        log.info("Signing in user with email [{}]", userData.getEmail());

        User user = findUserByEmailBoundary.execute(userData.getEmail());

        if (!nonNull(user)) {
            throw new ResourceViolationException("Invalid email or password");
        }

        boolean isPasswordValid = bcrypt.matches(userData.getPassword(), user.getPassword());

        if (!isPasswordValid) {
            throw new ResourceViolationException("Invalid email or password");
        }

        deleteUserRefreshTokensBoundary.execute(user.getId());

        AccessToken accessToken = generateAccessTokenUseCase.execute(user);
        RefreshToken refreshToken = generateRefreshTokenUseCase.execute(user);

        return AuthorizationData.builder()
                .accessToken(accessToken.getEncodedToken())
                .refreshToken(refreshToken.getEncodedToken())
                .build();
    }
}
