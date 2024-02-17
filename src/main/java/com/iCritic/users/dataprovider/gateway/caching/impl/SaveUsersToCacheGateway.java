package com.iCritic.users.dataprovider.gateway.caching.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.SaveUsersToCacheBoundary;
import com.iCritic.users.dataprovider.gateway.caching.util.CachingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class SaveUsersToCacheGateway implements SaveUsersToCacheBoundary {

    private final Jedis jedis;

    private final ObjectMapper objectMapper;

    public void execute(Page<User> users) {
        String cacheKey = CachingUtils.buildPaginationCachekey("users", users.getNumber(), users.getSize());

        log.info("Saving users to redis cache with cacheKey: [{}]", cacheKey);

        try {
            String jsonList = objectMapper.writeValueAsString(users.getContent());

            jedis.set(cacheKey, jsonList);
            jedis.pexpire(cacheKey, TimeUnit.MINUTES.toMillis(15));
        } catch (Exception e) {
            log.error("Error when saving users list to redis cache with cacheKey: [{}]", cacheKey, e);
        }
    }
}
