package com.gathering.challenge.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.challenge.model.entity.ChallengeStatus;
import com.gathering.challengeuser.model.domain.ChallengeUserDomain;
import com.gathering.gathering.model.domain.GatheringDomain;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.model.entity.ReadingTimeGoal;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.gathering.challenge.model.entity.ChallengeStatus.INACTIVE;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChallengeDomain {
    private Long id;
    private GatheringDomain gathering;
    private List<ChallengeUserDomain> challengeUsers;
    private ChallengeStatus challengeStatus;
    private double completeRate;    // 챌린지 전체 달성률 ( 유저 비율당 계산 예정 )
    private LocalDate startDate;
    private LocalDate endDate;  // 챌린지 종료일
    private ReadingTimeGoal readingTimeGoal;

    public static ChallengeDomain create(GatheringCreate gatheringCreate) {
        return ChallengeDomain.builder()
                .challengeUsers(new ArrayList<>())
                .challengeStatus(INACTIVE)
                .completeRate(0.0)
                .startDate(gatheringCreate.getStartDate())
                .endDate(gatheringCreate.getEndDate())
                .readingTimeGoal(gatheringCreate.getReadingTimeGoal())
                .build();
    }
}
