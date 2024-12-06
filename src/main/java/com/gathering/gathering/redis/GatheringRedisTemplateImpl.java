package com.gathering.gathering.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.gathering.gathering.redis.GatheringRedisKey.GATHERING_VIEW_COUNT_PREFIX;

@Component
@RequiredArgsConstructor
public class GatheringRedisTemplateImpl implements GatheringRedisTemplate {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void increment(String key) {
        redisTemplate.opsForValue().increment(key);
    }

    @Override
    public boolean isDuplicate(String dupKey) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(dupKey));
    }

    @Override
    public Long getLongValue(String key) {
        return Long.valueOf(redisTemplate.opsForValue().get(key));
    }

    @Override
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Set<String> getExpiredKey(String expiredKey) {
        return redisTemplate.keys(GATHERING_VIEW_COUNT_PREFIX + "*");
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public Boolean hasKey(String dupKey) {
        return redisTemplate.hasKey(dupKey);
    }

    @Override
    public void saveKeyWithExpire(String key, String isTrue, long expireTime, TimeUnit seconds) {
        redisTemplate.opsForValue().set(key, isTrue, expireTime, seconds);
    }

    @Override
    public void saveList(String key, String value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public void expire(String key, long expireTime, TimeUnit seconds) {
        redisTemplate.expire(key, expireTime, seconds);
    }

    @Override
    public List<String> getList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    @Override
    public long getListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public List<String> getListRange(String key, long offset, long size) {
        return redisTemplate.opsForList().range(key, offset, size);
    }

    @Override
    public void clearListRange(String key, long totalSize) {
        redisTemplate.opsForList().trim(key, totalSize, -1);
    }
}
