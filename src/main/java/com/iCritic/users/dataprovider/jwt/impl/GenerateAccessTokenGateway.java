package com.iCritic.users.dataprovider.jwt.impl;

import com.iCritic.users.config.properties.ApplicationProperties;
import com.iCritic.users.core.model.AccessToken;
import com.iCritic.users.core.usecase.boundary.GenerateAccessTokenBoundary;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;

@Component
public class GenerateAccessTokenGateway implements GenerateAccessTokenBoundary {

    @Autowired
    private ApplicationProperties applicationProperties;

    public String execute(AccessToken accessToken) {
        Claims claims = Jwts.claims();

        accessToken.getClaims().forEach(claim -> {
            claims.put(claim.getName(), claim.getValue());
        });

        return Jwts.builder()
                .setClaims(claims)
                .setId(accessToken.getId())
                .setIssuedAt(Date.from(accessToken.getIssuedAt().atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(accessToken.getExpiresAt().atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, applicationProperties.getJwtSecret())
                .compact();
    }
}
