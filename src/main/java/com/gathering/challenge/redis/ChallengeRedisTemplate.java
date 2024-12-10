package com.gathering.challenge.redis;

import java.util.concurrent.TimeUnit;

public interface ChallengeRedisTemplate {
    void saveKeyWithExpire(String challengeWaitingKey, String isTrue, long expireTime, TimeUnit seconds);
}
