package com.gathering.gathering.repository.search.util;

import com.gathering.gathering.model.dto.GatheringSearch;
import com.gathering.gathering.model.entity.GatheringStatus;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Component;

import static com.gathering.gathering.model.entity.QGathering.gathering;
import static com.gathering.gathering.model.entity.QGatheringUser.gatheringUser;

@Component
public class GatheringSearchConditionBuilder {

    public BooleanBuilder buildConditionAll(GatheringSearch gatheringSearch) {
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
            builder.and(gathering.challenge.readingTimeGoal.in(gatheringSearch.getReadingTimeGoals()));
        }

        if (gatheringSearch.getGatheringStatus() != null) {
            builder.and(gathering.gatheringStatus.eq(gatheringSearch.getGatheringStatus()));
        }

        return builder;
    }

    public BooleanBuilder buildGatheringAndUserStatus(GatheringStatus gatheringStatus, GatheringUserStatus gatheringUserStatus) {
        BooleanBuilder builder = new BooleanBuilder();
        if(gatheringStatus != null) {
            builder.and(gathering.gatheringStatus.eq(gatheringStatus));
        }

        if(gatheringUserStatus != null) {
            builder.and(gatheringUser.gatheringUserStatus.eq(gatheringUserStatus));
        }
        return builder;
    }
}
