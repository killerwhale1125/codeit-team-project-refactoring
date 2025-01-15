package com.gathering.challenge.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.challenge.infrastructure.entity.ChallengeStatus;
import com.gathering.challengeuser.domain.ChallengeUserDomain;
import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.gathering.domain.GatheringCreate;
import com.gathering.gathering.domain.ReadingTimeGoal;
import com.gathering.user.domain.UserDomain;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.gathering.challenge.infrastructure.entity.ChallengeStatus.COMPLETED;
import static com.gathering.challenge.infrastructure.entity.ChallengeStatus.INACTIVE;
import static com.gathering.common.base.response.BaseResponseStatus.USER_NOT_IN_CHALLENGE;

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

    public static ChallengeUserDomain leave(ChallengeDomain challenge, UserDomain user) {
        List<ChallengeUserDomain> challengeUsers = challenge.getChallengeUsers();
        ChallengeUserDomain challengeUser = challengeUsers.stream()
                .filter(cu -> cu.getUser().getId() == user.getId())
                .findFirst()
                .orElseThrow(() -> new BaseException(USER_NOT_IN_CHALLENGE));
        challengeUsers.remove(challengeUser);
        return challengeUser;
    }

    public static ChallengeDomain end(ChallengeDomain challenge) {
        challenge.challengeStatus = COMPLETED;
        return challenge;
    }
}
