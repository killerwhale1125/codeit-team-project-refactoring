package com.gathering.gathering.service;

import com.gathering.gathering.redis.GatheringRedisKey;
import com.gathering.gathering.redis.GatheringRedisTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static com.gathering.gathering.redis.GatheringRedisKey.GATHERING_INCREMENT_VIEW_COUNT_EXPIRATION;

@Service
@RequiredArgsConstructor
public class GatheringSearchAsync {
    private final GatheringRedisTemplate gatheringRedisTemplate;

    @Async
    @Transactional
    public void incrementViewCount(Long gatheringId, String userKey) {
        String dupKey = GatheringRedisKey.generatedGatheringDuplicateKey(gatheringId, userKey);

        // 조회 하지 않았을 경우에만 조회수 증가
        if (!isDuplicateRequest(dupKey)) {
            // 게시물 총 조회수 키 생성 gathering:view:{gathering_id}
            String viewCountKey = GatheringRedisKey.generatedViewCountKey(gatheringId);
            // 조회수 만료 키 생성 gathering:view:expire:{gathering_id}
            String expireKey = GatheringRedisKey.generatedExpireViewCount(gatheringId);

            gatheringRedisTemplate.increment(viewCountKey);

            // 값이 없었다면 만료 전용 키 생성
            if(!gatheringRedisTemplate.hasKey(expireKey)) {
                gatheringRedisTemplate.saveKeyWithExpire(expireKey, "1", GATHERING_INCREMENT_VIEW_COUNT_EXPIRATION, TimeUnit.SECONDS);
            }

            // 중복 요청 방지 저장
            gatheringRedisTemplate.saveKeyWithExpire(dupKey, "1", GatheringRedisKey.DUPLICATE_CHECK_EXPIRATION, TimeUnit.SECONDS);
        }
    }

    private boolean isDuplicateRequest(String dupKey) {
        return Boolean.TRUE.equals(gatheringRedisTemplate.hasKey(dupKey));
    }
}
