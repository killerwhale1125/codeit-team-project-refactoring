package com.gathering.gathering.scheduler;

import com.gathering.gathering.redis.GatheringRedisKey;
import com.gathering.gathering.redis.GatheringRedisTemplate;
import com.gathering.gathering.repository.GatheringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gathering.gathering.redis.GatheringRedisKey.GATHERING_INCREMENT_VIEW_COUNT_EXPIRATION;

@Component
@RequiredArgsConstructor
public class GatheringScheduler {

    private final GatheringRedisTemplate gatheringRedisTemplate;
    private final GatheringRepository gatheringRepository;
    private final JdbcTemplate jdbcTemplate;

    @Scheduled(fixedRate = GATHERING_INCREMENT_VIEW_COUNT_EXPIRATION) // 6시간마다 실행
    public void syncExpiredViewsToDB() {
        // 배치 사이즈 설정 (예: 500개씩 처리)
        int batchSize = 500;

        long totalSize = gatheringRedisTemplate.getListSize(GatheringRedisKey.GATHERING_EXPIRED_VIEW_COUNT_LIST_PREFIX);
        long offset = 0;
        while (offset < totalSize) {
            // 리스트에서 배치만큼 조회
            List<String> expiredKeysBatch = gatheringRedisTemplate
                    .getListRange(GatheringRedisKey.GATHERING_EXPIRED_VIEW_COUNT_LIST_PREFIX, offset, offset + batchSize - 1);

            // 배치 처리 로직
            processExpiredKeys(expiredKeysBatch);

            // 다음 배치로 이동
            offset += batchSize;
        }
        gatheringRedisTemplate.clearListRange(GatheringRedisKey.GATHERING_EXPIRED_VIEW_COUNT_LIST_PREFIX, totalSize);
    }

    private void processExpiredKeys(List<String> expiredKeysBatch) {
        // gatheringId와 viewCount를 한 번만 split하여 Map을 생성
        Map<Long, Long> gatheringIdToViewCount = expiredKeysBatch.stream()
                .map(key -> key.split(":")) // 각 key를 ":"로 분리
                .filter(parts -> parts.length == 2)
                .collect(Collectors.toMap(
                        parts -> Long.parseLong(parts[0]), // gatheringId를 Long으로 변환
                        parts -> Long.parseLong(parts[1])  // viewCount를 Long으로 변환
                ));

        List<Long> gatheringIds = gatheringIdToViewCount.keySet().stream().collect(Collectors.toList());

        // 배치 업데이트를 위한 SQL
        String sql = "UPDATE gathering SET view_count = view_count + ? WHERE gathering_id = ?";

        List<Object[]> batchArgs = new ArrayList<>();
        gatheringRepository.findByIdIn(gatheringIds).stream()
                .filter(gathering -> gatheringIdToViewCount.containsKey(gathering.getId())) // gatheringId가 존재하는 경우만 필터링. Map이라 조회 시간 O(1)
                .forEach(gathering -> {
                    Long expiredViewCount = gatheringIdToViewCount.get(gathering.getId());
                    if (expiredViewCount != null) {
                        long currentViewCount = gathering.getViewCount();
                        long deltaViewCount = expiredViewCount - currentViewCount;

                        // 배치 인수로 추가
                        batchArgs.add(new Object[]{deltaViewCount, gathering.getId()});
                    }
                });

        // 배치로 업데이트 실행
        if (!batchArgs.isEmpty()) {
            jdbcTemplate.batchUpdate(sql, batchArgs);
        }
    }
}
