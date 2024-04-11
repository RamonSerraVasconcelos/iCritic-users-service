package com.iCritic.users.entrypoint.fixture;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenFixture {

    public static String SECRET = "secret";
    public static String REFRESH_SECRET = "refreshSecret";
    public static int EXPIRATION = 20000;
    public static int REFRESH_EXPIRATION = 200000;

    public static String load() {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .claim("userId", 1L)
                .claim("role", "DEFAULT")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(LocalDateTime.now().plusSeconds(EXPIRATION).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public static String loadRefreshToken() {
        Date expirationDate = Date.from(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .claim("userId", 1L)
                .signWith(SignatureAlgorithm.HS512, REFRESH_SECRET)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .compact();
    }

    public static String loadUnsignedToken() {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .claim("userId", 1L)
                .claim("role", "DEFAULT")
                .compact();
    }
}
