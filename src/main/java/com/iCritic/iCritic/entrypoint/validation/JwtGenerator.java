package com.iCritic.iCritic.entrypoint.validation;

import com.iCritic.iCritic.core.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@Component
public class JwtGenerator {

    @Value("${spring.app.jwt.secret}")
    private String secret;

    @Value("${spring.app.jwt.expiration}")
    private int expiration;

    private static final Logger logger = LoggerFactory.getLogger(JwtGenerator.class);

    public String generateToken(User user) {
        return Jwts.builder()
                .setId(user.getId().toString())
                .setSubject(user.getRole().toString())
                .setIssuedAt(new Date())
                .setExpiration(new java.util.Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String getUserIdFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getId();
    }

    public String getUserRoleFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }
}
