package com.gathering.gathering.util;

import com.gathering.gathering.domain.GatheringSortType;
import com.gathering.gathering_review.domain.GatheringReviewSortType;
import com.querydsl.jpa.impl.JPAQuery;

import static com.gathering.challenge.infrastructure.entity.QChallenge.challenge;
import static com.gathering.gathering_review.infrastructure.entity.QGatheringReview.gatheringReview;

public class GatheringSortUtil {
    public static void applySorting(JPAQuery<?> query, GatheringSortType gatheringSortType) {
        if (gatheringSortType == null) return;

        switch (gatheringSortType) {
            case DEADLINE_ASC -> query.orderBy(challenge.endDate.asc());
            case NEWEST_FIRST -> query.orderBy(challenge.createdTime.desc());
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
