package com.iCritic.users.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RefreshToken {

    private String encodedToken;
    private String id;
    private User user;
    private List<Claim> claims;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private boolean active;
}
