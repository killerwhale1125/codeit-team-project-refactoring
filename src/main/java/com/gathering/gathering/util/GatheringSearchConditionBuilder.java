package com.gathering.gathering.util;

import com.gathering.gathering.domain.GatheringSearch;
import com.gathering.gathering.domain.GatheringStatus;
import com.gathering.gathering.domain.ReadingTimeGoal;
import com.gathering.gathering_user.domain.GatheringUserStatus;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static com.gathering.challenge.infrastructure.entity.QChallenge.challenge;
import static com.gathering.gathering.infrastructure.entity.QGathering.gathering;
import static com.gathering.gathering_user.infrastructure.entity.QGatheringUser.gatheringUser;

@Component
public class GatheringSearchConditionBuilder {

    public BooleanBuilder challengeSearchCondition(
            LocalDate startDate,
            LocalDate endDate,
            List<ReadingTimeGoal> readingTimeGoals
    ) {
        BooleanBuilder builder = new BooleanBuilder();
        if (startDate != null && endDate != null) {
            builder.and(challenge.startDate.between(startDate, endDate));
        }
        else if (startDate != null) {
            builder.and(challenge.startDate.goe(startDate));
        }
        else if (endDate != null) {
            builder.and(challenge.startDate.loe(endDate));
        }

        if (readingTimeGoals != null) {
            builder.and(challenge.readingTimeGoal.in(readingTimeGoals));
        }
        return builder;
    }

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
