package com.iCritic.users.dataprovider.gateway.caching.util;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CachingUtils {

    public static String buildPaginationCachekey(String mainKey, int page, int size) {
        return mainKey + ":" + String.valueOf(page) + ":" + String.valueOf(size);
    }
}
