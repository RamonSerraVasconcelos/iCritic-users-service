package com.iCritic.users.dataprovider.jwt;

import com.iCritic.users.config.properties.ApplicationProperties;
import com.iCritic.users.core.model.User;
import com.iCritic.users.dataprovider.gateway.database.entity.UserEntity;
import com.iCritic.users.dataprovider.gateway.database.mapper.UserEntityMapper;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private JwtManager jwtManager;

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    public String generateToken(User user) {
        jwtManager.revokeUserTokens(user.getId());

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
        String id = UUID.randomUUID().toString();
        Date issuedAt = new Date();
        Date expiresAt = new Date(System.currentTimeMillis() + applicationProperties.getJwtRefreshExpiration());

        UserEntity userEntity = UserEntityMapper.INSTANCE.userToUserEntity(user);

        Instant issuedAtInstant = issuedAt.toInstant();
        Instant expiresAtInstant = expiresAt.toInstant();
        LocalDateTime issuedAtLocalDate = issuedAtInstant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime expiresAtLocalDate = expiresAtInstant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        jwtManager.saveRefreshToken(id, userEntity, issuedAtLocalDate, expiresAtLocalDate);

        return Jwts.builder()
                .setId(id)
                .claim("userId", user.getId().toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.HS512, applicationProperties.getJwtRefreshSecret())
                .compact();
    }

    public boolean validateToken(String token) {
        JwtParser jwtParser = createTokenParser();

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

    public boolean validateRefreshToken(String token) {
        JwtParser jwtParser = createRefreshTokenParser();

        try {
            jwtParser.parseClaimsJws(token);

            if(!jwtManager.isTokenActive(getRefreshTokenId(token))) {
                jwtManager.revokeUserTokens(Long.parseLong(getUserIdFromRefreshToken(token)));
                return false;
            }

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

    public String getTokenId(String token) {
        return Jwts.parser().setSigningKey(applicationProperties.getJwtSecret()).parseClaimsJws(token).getBody().getId();
    }

    public String getUserIdFromToken(String token) {
        return Jwts.parser().setSigningKey(applicationProperties.getJwtSecret()).parseClaimsJws(token).getBody().get("userId").toString();
    }

    public String getUserRoleFromToken(String token) {
        return Jwts.parser().setSigningKey(applicationProperties.getJwtSecret()).parseClaimsJws(token).getBody().get("role").toString();
    }

    private JwtParser createTokenParser() {
        return Jwts.parser().setSigningKey(applicationProperties.getJwtSecret());
    }

    public String getRefreshTokenId(String token) {
        return Jwts.parser().setSigningKey(applicationProperties.getJwtRefreshSecret()).parseClaimsJws(token).getBody().getId();
    }

    public String getUserIdFromRefreshToken(String token) {
        return Jwts.parser().setSigningKey(applicationProperties.getJwtRefreshSecret()).parseClaimsJws(token).getBody().get("userId").toString();
    }

    private JwtParser createRefreshTokenParser() {
        return Jwts.parser().setSigningKey(applicationProperties.getJwtRefreshSecret());
    }
}
