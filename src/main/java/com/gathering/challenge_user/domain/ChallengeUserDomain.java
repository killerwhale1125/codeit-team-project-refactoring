package com.gathering.challenge_user.domain;

import com.gathering.challenge.domain.ChallengeDomain;
import com.gathering.gathering.domain.GatheringWeek;
import com.gathering.user.domain.UserDomain;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ChallengeUserDomain {
    private Long id;
    private ChallengeDomain challenge;
    private UserDomain user;
    private LocalDate attendanceDate;   // 출석 날짜
    private double attendanceRate;  // 출석률
    private double readingRate; // 독서 달성률

    public static ChallengeUserDomain create(UserDomain userDomain, ChallengeDomain challenge) {
        return ChallengeUserDomain.builder()
                .challenge(challenge)
                .attendanceDate(null)
                .attendanceRate(0.0)
                .readingRate(0.0)
                .user(userDomain)
                .build();
    }

    public void updateReadingRate(GatheringWeek gatheringWeek, long userAttendanceCount) {
        this.readingRate = ((double) userAttendanceCount / gatheringWeek.getWeek()) * 100;
    }
}
