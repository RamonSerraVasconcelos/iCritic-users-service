package com.iCritic.users.core.usecase;

import com.iCritic.users.core.model.AccessToken;
import com.iCritic.users.core.usecase.boundary.ValidateAccessTokenBoundary;
import com.iCritic.users.core.usecase.boundary.ValidateDecryptedTokenBoundary;
import com.iCritic.users.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidateAccessTokenUseCase {

    private final ValidateAccessTokenBoundary validateAccessTokenBoundary;

    private final ValidateDecryptedTokenBoundary validateDecryptedTokenBoundary;

    public AccessToken execute(String accessToken) {
        try {
            if (isTokenSigned(accessToken)) {
                return validateAccessTokenBoundary.execute(accessToken);
            }

            return validateDecryptedTokenBoundary.execute(accessToken);
        } catch (Exception e) {
            log.error("Failed validating access token", e);
            throw new UnauthorizedAccessException("Invalid access token");
        }
    }

    private boolean isTokenSigned(String token) {
        String[] parts = token.split("\\.");
        String header = new String(Base64.getUrlDecoder().decode(parts[0]));

        return header.contains("\"alg\":\"HS");
    }
}
