package com.gathering.gathering.redis;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface GatheringRedisTemplate {
    void increment(String key);

    boolean isDuplicate(String dupKey);

    Long getLongValue(String key);

    public String getValue(String key);

    Set<String> getExpiredKey(String expiredKey);

    void delete(String key);

    Boolean hasKey(String dupKey);

    void saveKeyWithExpire(String key, String s, long expireTime, TimeUnit seconds);

    void saveList(String expiredKey, String value);

    void expire(String key, long expireTime, TimeUnit seconds);

    List<String> getList(String gatheringExpiredViewCountListPrefix);

    long getListSize(String key);

    List<String> getListRange(String key, long offset, long size);

    void clearListRange(String key, long totalSize);
}
