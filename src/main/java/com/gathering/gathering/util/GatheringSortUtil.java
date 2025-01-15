package com.gathering.gathering.util;

import com.gathering.gathering.domain.GatheringReviewSortType;
import com.gathering.gathering.domain.GatheringSortType;
import com.querydsl.jpa.impl.JPAQuery;

import static com.gathering.gathering.infrastructure.entity.QGathering.*;
import static com.gathering.gathering_review.infrastructure.entity.QGatheringReview.gatheringReview;

public class GatheringSortUtil {
    public static void applySorting(JPAQuery<?> query, GatheringSortType gatheringSortType) {
        if (gatheringSortType == null) return;

        switch (gatheringSortType) {
            case DEADLINE_ASC -> query.orderBy(gathering.endDate.asc());
            case PARTICIPANTS_DESC -> query.orderBy(gathering.challenge.challengeUsers.size().desc());
            case VIEWS_DESC -> query.orderBy(gathering.viewCount.desc());
            case NEWEST_FIRST -> query.orderBy(gathering.createdTime.desc());
        }
    }

    /**
     * 모임 리뷰 정렬
     */
    public static void applyReviewSorting(JPAQuery<?> query, GatheringReviewSortType gatheringReviewSortType) {
        if (gatheringReviewSortType == null) return;

        switch (gatheringReviewSortType) {
            case DEADLINE_ASC -> query.orderBy(gatheringReview.createdTime.desc());
            case SCORE_DESC -> query.orderBy(gatheringReview.score.desc());
            case SCORE_ASC -> query.orderBy(gatheringReview.score.asc());
        }
    }
}
