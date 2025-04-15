package com.gathering.gathering.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class GatheringJoinLockManager {
    private static final ConcurrentHashMap<Long, LockWithCounter> joinLockMap = new ConcurrentHashMap<>();

    private static class LockWithCounter {
        final ReentrantLock lock = new ReentrantLock(true); // 공정 모드 락 설정 ( 우선 순위 보장 )
        final AtomicInteger count = new AtomicInteger(0);
    }

    /**
     * 각 모임 Id 별 Lock 분산으로 인해 Lock 경합 감소
     * ConcurrentHashMap으로 동일한 모임 ID에 대해 여러개의 new Map이 생성되는 동시성 문제를 방지
     */
    public static ReentrantLock acquireLock(Long gatheringId) {
        LockWithCounter wrapper = joinLockMap.computeIfAbsent(gatheringId, id -> new LockWithCounter());
        wrapper.count.incrementAndGet(); // ref count 증가
        return wrapper.lock;
    }

    /**
     * 안전한 Lock 반납 및 해제를 위한 CAS 원자성 연산으로 자원 해제
     */
    public static void releaseLock(Long gatheringId) {
        LockWithCounter wrapper = joinLockMap.get(gatheringId);

        if (wrapper != null) {
            int remaining = wrapper.count.decrementAndGet(); // ref count 감소
            if (remaining == 0) {
                joinLockMap.remove(gatheringId, wrapper); // 사용 중인 스레드가 없으면 제거
            }
        }
    }
}
