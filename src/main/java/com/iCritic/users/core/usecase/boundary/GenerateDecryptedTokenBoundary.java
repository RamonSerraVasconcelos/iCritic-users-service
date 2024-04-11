package com.iCritic.users.core.usecase.boundary;

import com.iCritic.users.core.enums.Role;

import java.util.Date;

public interface GenerateDecryptedTokenBoundary {

    String execute(String tokenId, Long userId, Role role, Date issuedAt, Date expiration);
}
