package com.iCritic.users.core.utils;

import com.iCritic.users.core.model.Claim;

import java.util.List;

public class TokenUtils {

    public static Claim getClaim(List<Claim> claims, String wantedClaim) {
        return claims.stream()
                .filter(claim -> wantedClaim.equals(claim.getName()))
                .findFirst()
                .orElse(null);
    }
}
