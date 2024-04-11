package com.iCritic.users.dataprovider.gateway.caching.impl;

import com.iCritic.users.core.usecase.boundary.CheckUsersBlacklistBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckUsersBlacklistGateway implements CheckUsersBlacklistBoundary {

    private final Jedis jedis;

    private static final String KEY_PREFIX = "users:blacklist:";

    public boolean isUserBlackListed(Long userId) {
        return jedis.exists(KEY_PREFIX.concat(String.valueOf(userId)));
    }
}
