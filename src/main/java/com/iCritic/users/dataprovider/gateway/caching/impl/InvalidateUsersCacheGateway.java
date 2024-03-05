package com.iCritic.users.dataprovider.gateway.caching.impl;

import com.iCritic.users.core.usecase.boundary.InvalidateUsersCacheBoundary;
import com.iCritic.users.dataprovider.gateway.caching.InvalidatePaginationCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvalidateUsersCacheGateway implements InvalidateUsersCacheBoundary {

    private final InvalidatePaginationCache invalidatePaginationCache;

    public void execute() {
        try {
            log.info("Invalidating users cache");

            invalidatePaginationCache.execute("users");
        } catch (Exception e) {
            log.error("Error invalidating users cache", e);
        }
    }
}
