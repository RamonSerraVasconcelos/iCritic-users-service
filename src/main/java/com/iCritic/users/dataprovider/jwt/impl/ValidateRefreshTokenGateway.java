package com.iCritic.users.dataprovider.jwt.impl;

import com.iCritic.users.config.properties.ApplicationProperties;
import com.iCritic.users.core.model.Claim;
import com.iCritic.users.core.model.RefreshToken;
import com.iCritic.users.core.usecase.boundary.ValidateRefreshTokenBoundary;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ValidateRefreshTokenGateway implements ValidateRefreshTokenBoundary {

    @Autowired
    private ApplicationProperties applicationProperties;

    public RefreshToken execute(String encodedToken) {
        JwtParser jwtParser = Jwts.parser().setSigningKey(applicationProperties.getJwtRefreshSecret());

        try {
            Claims claims = jwtParser.parseClaimsJws(encodedToken).getBody();

            return RefreshToken.builder()
                    .id(getId(claims))
                    .claims(getClaimsList(claims))
                    .issuedAt(getIssuedAt(claims))
                    .expiresAt(getExpiresAt(claims))
                    .build();
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return null;
    }

    private String getId(Claims claims) {
        return claims.getId();
    }

    private LocalDateTime getIssuedAt(Claims claims) {
        return claims.getIssuedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private LocalDateTime getExpiresAt(Claims claims) {
        return claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private List<Claim> getClaimsList(Claims claims) {
        List<Claim> claimList = new ArrayList<>();

        claims.forEach((name, value) -> {
            claimList.add(Claim.builder().name(name).value(value.toString()).build());
        });

        return claimList;
    }
}
