package com.gathering.gathering.redis;

public class GatheringRedisKey {

    /**
     * =============================== 조회수 전용 키값 ===============================
     */
    // 화면에 보여질 view 키
    public static final String GATHERING_VIEW_COUNT_PREFIX = "gathering:view:";
    // 조회수가 만료될 키
    public static final String GATHERING_EXPIRE_VIEW_COUNT_PREFIX = "gathering:view:expire:";
    // 이미 조회수가 만료되어 삭제된 키값이 저장될 empty List 키
    public static final String GATHERING_EXPIRED_VIEW_COUNT_LIST_PREFIX = "gathering:view:expired";
    // 중복 사용자 조회 방지 키
    public static final String GATHERING_DUPLICATE_CHECK_PREFIX = "gathering:duplication:";
    // 중복 사용자 조회 방지 키 만료 시간
    public static final long DUPLICATE_CHECK_EXPIRATION = 600L; // 5분
    // 조회수가 만료될 키의 만료시간
    public static final long GATHERING_INCREMENT_VIEW_COUNT_EXPIRATION = 21600L; // 6시간
//    public static final long GATHERING_INCREMENT_VIEW_COUNT_EXPIRATION = 10; // 6시간

    public static String generatedViewCountKey(Long gatheringId) {
        return GATHERING_VIEW_COUNT_PREFIX + gatheringId;
    }

    public static String generatedGatheringDuplicateKey(Long gatheringId, String userKey) {
        return GATHERING_DUPLICATE_CHECK_PREFIX + gatheringId + ":" + userKey;
    }

    public static String generatedExpireViewCount(Long gatheringId) {
        return GATHERING_EXPIRE_VIEW_COUNT_PREFIX + gatheringId;
    }
    /**
     * =============================================================================
     */

}
