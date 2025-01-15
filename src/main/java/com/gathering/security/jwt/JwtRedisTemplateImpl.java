package com.gathering.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtRedisTemplateImpl implements JwtRedisTemplate{
    private final RedisTemplate<String, String> redisTemplate;


    @Override
    public Boolean hasKey(String refreshToken) {
        return redisTemplate.hasKey(refreshToken);
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        redisTemplate.delete(refreshToken);
    }

    @Override
    public void saveRefreshToken(String username, String refreshToken, int day, TimeUnit days) {
        redisTemplate.opsForValue().set(username, refreshToken, day, days);
    }

    @Override
    public Optional<String> getRefreshToken(String username) {
        return Optional.of(redisTemplate.opsForValue().get(username));
    }
}
