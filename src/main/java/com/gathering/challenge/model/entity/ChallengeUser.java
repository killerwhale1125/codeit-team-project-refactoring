package com.gathering.challenge.model.entity;

import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.user.model.entitiy.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    private LocalDateTime attendanceDate;   // 출석 날짜
    private double attendanceRate;

    public static ChallengeUser createChallengeUser(User user) {
        ChallengeUser challengeUser = new ChallengeUser();
        challengeUser.attendanceDate = null;
        challengeUser.attendanceRate = 0.0;
        challengeUser.addUser(user);
        return challengeUser;
    }

    public void addChallenge(Challenge challenge) {
        this.challenge = challenge;
        challenge.getChallengeUsers().add(this);
    }

    public void addUser(User user) {
        this.user = user;
        user.getChallengeUsers().add(this);
    }
}
