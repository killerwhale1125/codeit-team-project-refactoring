package com.gathering.challenge.redis;

import com.gathering.challenge.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import static com.gathering.gathering.redis.GatheringRedisKey.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChallengeMessageListener implements MessageListener {

    private final ChallengeService challengeService;
    
    // 챌린시 자동 시작
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("Expired key detected: {}", expiredKey);

        // 특정 키 패턴만 처리
        if (expiredKey.startsWith(ChallengeRedisKey.CHALLENGE_WAITING_PREFIX)) {
            // challengeId 추출
            String challengeId = expiredKey.split(":")[2];
            challengeService.start(Long.parseLong(challengeId));
        }
    }
}
