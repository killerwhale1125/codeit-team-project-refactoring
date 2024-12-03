package com.gathering.gathering.repository.search.util;

import com.gathering.gathering.model.dto.GatheringSearch;
import com.querydsl.core.BooleanBuilder;

import static com.gathering.gathering.model.entity.QGathering.gathering;

public class GatheringSearchConditionBuilder {
    private final GatheringSearch gatheringSearch;

    public GatheringSearchConditionBuilder(GatheringSearch gatheringSearch) {
        this.gatheringSearch = gatheringSearch;
    }

    public BooleanBuilder build() {
        BooleanBuilder builder = new BooleanBuilder();

        if (gatheringSearch.getBookTitle() != null && !gatheringSearch.getBookTitle().isEmpty()) {
            builder.and(gathering.book.title.containsIgnoreCase(gatheringSearch.getBookTitle()));
        }

        if (gatheringSearch.getStartDate() != null) {
            builder.and(gathering.challenge.startDateTime.goe(gatheringSearch.getStartDate()));
        }

        if (gatheringSearch.getEndDate() != null) {
            builder.and(gathering.challenge.endDateTime.loe(gatheringSearch.getEndDate()));
        }

        if (gatheringSearch.getGoalTime() != null && gatheringSearch.getGoalTime() > 0) {
            builder.and(gathering.challenge.goalTime.eq(gatheringSearch.getGoalTime()));
        }

        if (gatheringSearch.getGatheringStatus() != null) {
            builder.and(gathering.gatheringStatus.eq(gatheringSearch.getGatheringStatus()));
        }

        return builder;
    }
}
