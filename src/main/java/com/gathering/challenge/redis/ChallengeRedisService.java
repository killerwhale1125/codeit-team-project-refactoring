package com.gathering.challenge.redis;

import com.gathering.challenge.model.entity.Challenge;
import com.gathering.util.date.DateCalculateHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ChallengeRedisService {
    private final ChallengeRedisTemplate challengeRedisTemplate;
    private final DateCalculateHolder dateCalculateHolder;

    public void scheduleChallengeStateChange(Challenge challenge, LocalDate startDate) {
        long secondsUntilStart = dateCalculateHolder.calculateSecondsUntilStart(startDate, LocalDateTime.now());
        String challengeWaitingKey = ChallengeRedisKey.generateWaitingKey(challenge.getId());
        challengeRedisTemplate.saveKeyWithExpire(challengeWaitingKey, "1", secondsUntilStart, TimeUnit.SECONDS);
    }
}
