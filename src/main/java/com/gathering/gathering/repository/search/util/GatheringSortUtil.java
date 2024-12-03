package com.gathering.gathering.repository.search.util;

import com.gathering.gathering.model.entity.GatheringSortType;
import com.querydsl.jpa.impl.JPAQuery;

import static com.gathering.gathering.model.entity.QGathering.gathering;

public class GatheringSortUtil {
    public static void applySorting(JPAQuery<?> query, GatheringSortType gatheringSortType) {
        if (gatheringSortType == null) return;

        switch (gatheringSortType) {
            case DEADLINE_ASC -> query.orderBy(gathering.endDateTime.asc());
            case PARTICIPANTS_DESC -> query.orderBy(gathering.challenge.challengeUsers.size().desc());
            case VIEWS_DESC -> query.orderBy(gathering.viewCount.desc());
            case NEWEST_FIRST -> query.orderBy(gathering.createdTime.desc());
        }
    }
}
