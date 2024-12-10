package com.gathering.challenge.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ChallengeRedisTemplateImpl implements ChallengeRedisTemplate {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void saveKeyWithExpire(String key, String isTrue, long expireTime, TimeUnit seconds) {
        redisTemplate.opsForValue().set(key, isTrue, expireTime, seconds);
    }
}
