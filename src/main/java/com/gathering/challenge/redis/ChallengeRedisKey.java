package com.gathering.challenge.redis;

public class ChallengeRedisKey {

    public static final String CHALLENGE_WAITING_PREFIX = "challenge:wait:";

    public static String generateWaitingKey(Long challengeId) {
        return CHALLENGE_WAITING_PREFIX + challengeId;
    }
}
