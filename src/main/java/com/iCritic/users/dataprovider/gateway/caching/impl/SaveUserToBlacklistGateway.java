package com.iCritic.users.dataprovider.gateway.caching.impl;

import com.iCritic.users.core.usecase.boundary.SaveUserToBlacklistBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
@Slf4j
public class SaveUserToBlacklistGateway implements SaveUserToBlacklistBoundary {

    private final Jedis jedis;

    private static final String KEY_PREFIX = "users:blacklist:";

    public void execute(Long userId, int expirationDateTimeInSec) {
        log.info("Adding user [{}] to blacklist", userId);

        long expirationDateTimeInMillis = LocalDateTime
                .ofEpochSecond(expirationDateTimeInSec, 0, ZoneOffset.UTC)
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli();

        jedis.set(KEY_PREFIX.concat(userId.toString()), userId.toString());
        jedis.pexpire(KEY_PREFIX.concat(userId.toString()), expirationDateTimeInMillis);
    }
}
