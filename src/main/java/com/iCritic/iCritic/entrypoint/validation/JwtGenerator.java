package com.iCritic.iCritic.entrypoint.validation;

import com.iCritic.iCritic.config.properties.ApplicationProperties;
import com.iCritic.iCritic.core.model.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtGenerator {

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final Logger logger = LoggerFactory.getLogger(JwtGenerator.class);

    public String generateToken(User user) {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .claim("userId", user.getId().toString())
                .claim("role", user.getRole().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + applicationProperties.getJwtExpiration()))
                .signWith(SignatureAlgorithm.HS512, applicationProperties.getJwtSecret())
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .claim("userId", user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + applicationProperties.getJwtRefreshExpiration()))
                .signWith(SignatureAlgorithm.HS512, applicationProperties.getJwtSecret())
                .compact();
    }

    public boolean validateToken(String token) {
        JwtParser jwtParser = createJwtParser();

        try {
            jwtParser.parseClaimsJws(token);

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
        return Jwts.parser().setSigningKey(applicationProperties.getJwtSecret()).parseClaimsJws(token).getBody().getId();
    }

    public String getUserRoleFromToken(String token) {
        return Jwts.parser().setSigningKey(applicationProperties.getJwtSecret()).parseClaimsJws(token).getBody().getSubject();
    }

    private JwtParser createJwtParser() {
        return Jwts.parser().setSigningKey(applicationProperties.getJwtSecret());
    }
}
