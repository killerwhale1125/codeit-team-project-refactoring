package com.gathering.gathering.repository.search.util;

import com.gathering.gathering.model.dto.GatheringSearch;
import com.gathering.gathering.model.entity.GatheringStatus;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

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

        if (gatheringSearch.getReadingTimeGoals() != null) {
            builder.and(gathering.challenge.readingTimeGoal.in(gatheringSearch.getReadingTimeGoals()));
        }

        if (gatheringSearch.getGatheringStatus() != null) {
            builder.and(gathering.gatheringStatus.eq(gatheringSearch.getGatheringStatus()));
        }

        if(gatheringSearch.isToday()) {
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
