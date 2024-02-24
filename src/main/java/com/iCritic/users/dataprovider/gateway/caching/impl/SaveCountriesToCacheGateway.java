package com.iCritic.users.dataprovider.gateway.caching.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iCritic.users.core.model.Country;
import com.iCritic.users.core.usecase.boundary.SaveCountriesToCacheBoundary;
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
public class SaveCountriesToCacheGateway implements SaveCountriesToCacheBoundary {

    private final Jedis jedis;

    private final ObjectMapper objectMapper;

    public void execute(Page<Country> countries) {
        String cacheKey = CachingUtils.buildPaginationCachekey("countries", countries.getNumber(), countries.getSize());

        log.info("Saving countries to redis cache with cacheKey: [{}]", cacheKey);

        try {
            String jsonList = objectMapper.writeValueAsString(countries.getContent());

            jedis.set(cacheKey, jsonList);
            jedis.pexpire(cacheKey, TimeUnit.DAYS.toMillis(30));
        } catch (Exception e) {
            log.error("Error when saving countries list to redis cache with cacheKey: [{}]", cacheKey, e);
        }
    }
}
