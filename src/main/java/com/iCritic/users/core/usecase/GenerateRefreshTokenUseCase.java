package com.iCritic.users.core.usecase;

import com.iCritic.users.config.properties.ApplicationProperties;
import com.iCritic.users.core.model.Claim;
import com.iCritic.users.core.model.RefreshToken;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.GenerateRefreshTokenBoundary;
import com.iCritic.users.core.usecase.boundary.SaveRefreshTokenBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerateRefreshTokenUseCase {

    private final GenerateRefreshTokenBoundary generateRefreshTokenBoundary;

    private final SaveRefreshTokenBoundary saveRefreshTokenBoundary;

    @Autowired
    private ApplicationProperties applicationProperties;

    public RefreshToken execute(User user) {
        log.info("Generating refresh token for user with id: [{}]", user.getId());

        List<Claim> claims = List.of(
                Claim.builder().name("userId").value(user.getId().toString()).build()
        );

        RefreshToken refreshToken = RefreshToken.builder()
                .id(UUID.randomUUID().toString())
                .user(user)
                .claims(claims)
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusSeconds(applicationProperties.getJwtRefreshExpiration()))
                .active(true)
                .build();

        String encodedToken = generateRefreshTokenBoundary.execute(refreshToken);
        refreshToken.setEncodedToken(encodedToken);

        saveRefreshTokenBoundary.execute(refreshToken);

        return refreshToken;
    }
}
