package com.iCritic.users.dataprovider.jwt.impl;

import com.iCritic.users.core.model.AccessToken;
import com.iCritic.users.core.model.Claim;
import com.iCritic.users.core.usecase.boundary.ValidateAccessTokenBoundary;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ValidateAccessTokenGateway implements ValidateAccessTokenBoundary {

    public AccessToken execute(String encodedToken) {
        try {
            Claims claims = Jwts.parser().parseClaimsJwt(encodedToken).getBody();

            return AccessToken.builder()
                    .id(getId(claims))
                    .claims(getClaimsList(claims))
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

    private List<Claim> getClaimsList(Claims claims) {
        List<Claim> claimList = new ArrayList<>();

        claims.forEach((name, value) -> {
            claimList.add(Claim.builder().name(name).value(value.toString()).build());
        });

        return claimList;
    }
}
