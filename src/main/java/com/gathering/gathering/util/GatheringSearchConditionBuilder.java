package com.gathering.gathering.util;

import com.gathering.gathering.domain.GatheringSearch;
import com.gathering.gathering.domain.GatheringStatus;
import com.gathering.gatheringuser.domain.GatheringUserStatus;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.gathering.gathering.infrastructure.entity.QGathering.*;
import static com.gathering.gatheringuser.infrastructure.entity.QGatheringUser.gatheringUser;

@Component
public class GatheringSearchConditionBuilder {

    public BooleanBuilder buildConditionAll(GatheringSearch gatheringSearch) {
        BooleanBuilder builder = new BooleanBuilder();

        if (gatheringSearch.getBookTitle() != null && !gatheringSearch.getBookTitle().isEmpty()) {
            builder.and(gathering.book.title.containsIgnoreCase(gatheringSearch.getBookTitle()));
        }

        if (gatheringSearch.getStartDate() != null) {
            builder.and(gathering.challenge.startDate.goe(gatheringSearch.getStartDate()));
        }

        if (gatheringSearch.getEndDate() != null) {
            builder.and(gathering.challenge.endDate.loe(gatheringSearch.getEndDate()));
        }

        if (gatheringSearch.getReadingTimeGoals() != null) {
            builder.and(gathering.challenge.readingTimeGoal.in(gatheringSearch.getReadingTimeGoals()));
        }

        if (gatheringSearch.getGatheringStatus() != null) {
            builder.and(gathering.gatheringStatus.eq(gatheringSearch.getGatheringStatus()));
        }

        if(gatheringSearch.getToday() != null && gatheringSearch.getToday() == true) {
            builder.and(gathering.startDate.eq(LocalDate.now()));
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
