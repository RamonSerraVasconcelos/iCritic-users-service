package com.iCritic.users.dataprovider.jwt.impl;

import com.iCritic.users.config.properties.ApplicationProperties;
import com.iCritic.users.core.model.RefreshToken;
import com.iCritic.users.core.usecase.boundary.GenerateRefreshTokenBoundary;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class GenerateRefreshTokenGateway implements GenerateRefreshTokenBoundary {

    private final ApplicationProperties applicationProperties;

    public String execute(RefreshToken refreshToken) {
        Claims claims = Jwts.claims();

        refreshToken.getClaims().forEach(claim -> {
            claims.put(claim.getName(), claim.getValue());
        });

        return Jwts.builder()
                .setClaims(claims)
                .setId(refreshToken.getId())
                .setIssuedAt(Date.from(refreshToken.getIssuedAt().toInstant(ZoneOffset.UTC)))
                .setExpiration(Date.from(refreshToken.getExpiresAt().toInstant(ZoneOffset.UTC)))
                .signWith(SignatureAlgorithm.HS512, applicationProperties.getJwtRefreshSecret())
                .compact();
    }
}
