package com.gathering.gathering.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import static com.gathering.gathering.infrastructure.redis.GatheringRedisKey.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GatheringMessageListener implements MessageListener {

    private final GatheringRedisTemplate gatheringRedisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("Expired key detected: {}", expiredKey);

        // 특정 키 패턴만 처리
        if (expiredKey.startsWith(GATHERING_EXPIRE_VIEW_COUNT_PREFIX)) {
            // gatheringId 추출
            String gatheringId = expiredKey.split(":")[3];
            String count = gatheringRedisTemplate.getValue(GATHERING_VIEW_COUNT_PREFIX + gatheringId);
            // 처리 로직 예시: Redis 리스트에 추가
            gatheringRedisTemplate.saveList(GATHERING_EXPIRED_VIEW_COUNT_LIST_PREFIX, gatheringId + ":" + count);
        }
    }
}
