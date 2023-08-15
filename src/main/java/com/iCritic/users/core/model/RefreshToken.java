package com.iCritic.users.core.model;

import com.iCritic.users.dataprovider.gateway.database.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RefreshToken {

    private String id;
    private UserEntity userEntity;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private boolean active;
}
