package com.gathering.challenge.redis;

import com.gathering.common.base.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.gathering.common.base.response.BaseResponseStatus.REDIS_OPERATION_FAILED;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChallengeRedisTemplateImpl implements ChallengeRedisTemplate {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void saveKeyWithExpire(String key, String isTrue, long expireTime, TimeUnit seconds) {
        try {
            redisTemplate.opsForValue().set(key, isTrue, expireTime, seconds);
        } catch (DataAccessException e) {
            // Redis 작업 실패에 대한 로그
            log.error("Failed to save key '{}' with expiration to Redis. Reason: {}", key, e.getMessage(), e);

            throw new BaseException(REDIS_OPERATION_FAILED);
        }
    }
}
