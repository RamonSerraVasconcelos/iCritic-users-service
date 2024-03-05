package com.iCritic.users.dataprovider.gateway.caching.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iCritic.users.core.model.User;
import com.iCritic.users.core.usecase.boundary.FindUsersCachedBoundary;
import com.iCritic.users.dataprovider.gateway.caching.util.CachingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.List;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
@Slf4j
public class FindUsersCachedGateway implements FindUsersCachedBoundary {

    private final Jedis jedis;

    private final ObjectMapper objectMapper;

    public Page<User> execute(Pageable pageable) {
        String cacheKey = CachingUtils.buildPaginationCachekey("users", pageable.getPageNumber(), pageable.getPageSize());

        log.info("Fetching users from redis cache with cacheKey: [{}]", cacheKey);

        try {
            String usersJson = jedis.get(cacheKey);

            if (isNull(usersJson) || usersJson.isBlank()) {
                return null;
            }

            List<User> users = objectMapper.readValue(usersJson, new TypeReference<>() {
            });

            return new PageImpl<>(users, pageable, users.size());
        } catch (Exception e) {
            log.error("Error when fetching users from redis cache with cacheKey: [{}]", cacheKey, e);
            return null;
        }
    }
}
