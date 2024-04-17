package com.iCritic.users.core.usecase;

import com.iCritic.users.config.properties.ApplicationProperties;
import com.iCritic.users.core.model.AccessToken;
import com.iCritic.users.core.model.Claim;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.GenerateAccessTokenBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenerateAccessTokenUseCase {

    private final GenerateAccessTokenBoundary generateAccessTokenBoundary;

    private final ApplicationProperties applicationProperties;

    public AccessToken execute(User user) {
        log.info("Generating access token for user with id: [{}]", user.getId());

        List<Claim> claims = List.of(
                Claim.builder().name("userId").value(user.getId().toString()).build(),
                Claim.builder().name("role").value(user.getRole().name()).build()
        );

        AccessToken accessToken = AccessToken.builder()
                .id(UUID.randomUUID().toString())
                .claims(claims)
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusSeconds(applicationProperties.getJwtExpiration()))
                .build();

        String encodedToken = generateAccessTokenBoundary.execute(accessToken);
        accessToken.setEncodedToken(encodedToken);

        return accessToken;
    }
}
