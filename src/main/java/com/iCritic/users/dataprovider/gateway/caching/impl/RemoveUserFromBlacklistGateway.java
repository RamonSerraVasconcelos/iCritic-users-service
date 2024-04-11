package com.iCritic.users.dataprovider.gateway.caching.impl;

import com.iCritic.users.core.usecase.boundary.RemoveUserFromBlacklistBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
@RequiredArgsConstructor
@Slf4j
public class RemoveUserFromBlacklistGateway implements RemoveUserFromBlacklistBoundary {

    private final Jedis jedis;

    private static final String KEY_PREFIX = "users:blacklist:";

    public void execute(Long userId) {
        log.info("Removing user [{}] from blacklist", userId);

        jedis.del(KEY_PREFIX.concat(userId.toString()));
    }
}
