package com.iCritic.iCritic.entrypoint.fixture;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenFixture {

    public static String SECRET = "secret";
    public static int EXPIRATION = 20000;

    public static String load() {
        return Jwts.builder()
                .setSubject("user")
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }
}
