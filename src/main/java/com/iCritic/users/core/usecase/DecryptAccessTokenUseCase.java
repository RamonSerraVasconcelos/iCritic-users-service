package com.iCritic.users.core.usecase;

import com.iCritic.users.core.enums.Role;
import com.iCritic.users.core.model.AccessToken;
import com.iCritic.users.core.usecase.boundary.GenerateDecryptedTokenBoundary;
import com.iCritic.users.core.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class DecryptAccessTokenUseCase {

    private final ValidateAccessTokenUseCase validateAccessTokenUseCase;

    private final GenerateDecryptedTokenBoundary generateDecryptedTokenBoundary;

    public String execute(String encodedToken) {
        AccessToken accessToken = validateAccessTokenUseCase.execute(encodedToken);

        String userId = TokenUtils.getClaim(accessToken.getClaims(), "userId").getValue();
        String role = TokenUtils.getClaim(accessToken.getClaims(), "role").getValue();

        return generateDecryptedTokenBoundary.execute(accessToken.getId(), Long.valueOf(userId),
                Role.valueOf(role), toDate(accessToken.getIssuedAt()), toDate(accessToken.getExpiresAt()));
    }

    public static Date toDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();

        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }
}
