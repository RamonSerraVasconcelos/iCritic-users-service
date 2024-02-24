package com.iCritic.users.dataprovider.gateway.caching.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iCritic.users.core.model.Country;
import com.iCritic.users.core.usecase.boundary.FindCountriesCachedBoundary;
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
public class FindCountriesCachedGateway implements FindCountriesCachedBoundary {

    private final Jedis jedis;

    private final ObjectMapper objectMapper;

    public Page<Country> execute(Pageable pageable) {
        String cacheKey = CachingUtils.buildPaginationCachekey("countries", pageable.getPageNumber(), pageable.getPageSize());

        log.info("Fetching countries from redis cache with cacheKey: [{}]", cacheKey);

        try {
            String countriesJson = jedis.get(cacheKey);

            if (isNull(countriesJson) || countriesJson.isBlank()) {
                return null;
            }

            List<Country> countries = objectMapper.readValue(countriesJson, new TypeReference<>() {
            });

            return new PageImpl<>(countries, pageable, countries.size());
        } catch (Exception e) {
            log.error("Error when fetching countries from redis cache with cacheKey: [{}]", cacheKey, e);
            return null;
        }
    }
}
