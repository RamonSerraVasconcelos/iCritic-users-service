package com.iCritic.users.dataprovider.jwt.impl;

import com.iCritic.users.core.enums.Role;
import com.iCritic.users.core.usecase.boundary.GenerateDecryptedTokenBoundary;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class GenerateDecryptedTokenGateway implements GenerateDecryptedTokenBoundary {

    public String execute(String tokenId, Long userId, Role role, Date issuedAt, Date expiration) {
        return Jwts.builder()
                .setId(tokenId)
                .claim("userId", userId)
                .claim("role", role.name())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .compact();
    }
}
