package com.gathering.challengeuser.model.entity;

import com.gathering.challenge.model.entity.Challenge;
import com.gathering.challengeuser.model.domain.ChallengeUserDomain;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.model.entity.GatheringWeek;
import com.gathering.user.model.domain.UserDomain;
import com.gathering.user.model.entitiy.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
@Builder
public class ChallengeUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    private LocalDate attendanceDate;   // 출석 날짜
    private double attendanceRate;  // 출석률
    private double readingRate; // 독서 달성률

    public static ChallengeUser createChallengeUser(User user) {
//        ChallengeUser challengeUser = new ChallengeUser();
//        challengeUser.attendanceDate = null;
//        challengeUser.attendanceRate = 0.0;
//        challengeUser.readingRate = 0.0;
//        challengeUser.addUser(user);
        return null;
    }

    public static ChallengeUser from(ChallengeUserDomain challengeUserDomain) {
        return ChallengeUser.builder()
                .attendanceDate(challengeUserDomain.getAttendanceDate())
                .attendanceRate(challengeUserDomain.getAttendanceRate())
                .readingRate(challengeUserDomain.getReadingRate())
                .user(User.fromEntity(challengeUserDomain.getUser()))
                .build();
    }

    public void addChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void addUser(User user) {
        this.user = user;
        user.getChallengeUsers().add(this);
    }

    public void updateReadingRate(GatheringWeek gatheringWeek, long userAttendanceCount) {
        this.readingRate = userAttendanceCount / gatheringWeek.getWeek() * 100;
    }

    public ChallengeUserDomain toEntity() {
        return ChallengeUserDomain.builder()
                .id(id)
                .user(UserDomain.toEntity(user))
                .attendanceDate(attendanceDate)
                .attendanceRate(attendanceRate)
                .readingRate(readingRate)
                .build();
    }
}
