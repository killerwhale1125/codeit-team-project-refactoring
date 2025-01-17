package com.gathering.challenge_user.infrastructure.entity;

import com.gathering.challenge.infrastructure.entity.Challenge;
import com.gathering.challenge_user.domain.ChallengeUserDomain;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.user.infrastructure.entitiy.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.gathering.util.entity.EntityUtils.nullableEntity;
import static jakarta.persistence.Persistence.getPersistenceUtil;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static ChallengeUser fromEntity(ChallengeUserDomain challengeUser) {
        ChallengeUser challengeUserEntity = new ChallengeUser();
        challengeUserEntity.id = challengeUser.getId();
        challengeUserEntity.attendanceDate = challengeUser.getAttendanceDate();
        challengeUserEntity.attendanceRate = challengeUser.getAttendanceRate();
        challengeUserEntity.readingRate = challengeUser.getReadingRate();

        User user = nullableEntity(User::fromEntity, challengeUser.getUser());
        if(user != null) {
            challengeUserEntity.user = user;
            user.getChallengeUsers().add(challengeUserEntity);
        }

        Challenge challenge = nullableEntity(Challenge::fromEntity, challengeUser.getChallenge());
        if(challenge != null) {
            challengeUserEntity.challenge = challenge;
            challenge.getChallengeUsers().add(challengeUserEntity);
        }

        return challengeUserEntity;
    }

    public ChallengeUserDomain toEntity() {
        ChallengeUserDomain.ChallengeUserDomainBuilder builder = ChallengeUserDomain.builder()
                .id(id)
                .user(user.toEntity())
                .attendanceDate(attendanceDate)
                .attendanceRate(attendanceRate)
                .readingRate(readingRate);

        if (user != null && getPersistenceUtil().isLoaded(user)) {
            builder.user(user.toEntity());
        }

        return builder.build();
    }
}
