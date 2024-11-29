package com.gathering.challenge.model.entity;

import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.model.entity.Gathering;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long id;

    @OneToOne(mappedBy = "challenge", fetch = FetchType.LAZY)
    private Gathering gathering;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<ChallengeUser> challengeUsers = new ArrayList<>();

    @Enumerated
    private ChallengeStatus challengeStatus;

    private double completeRate;
    private LocalDateTime endDateTime;

    public static Challenge createChallenge(LocalDateTime endDate, ChallengeUser challengeUsers) {
        Challenge challenge = new Challenge();
        challenge.challengeStatus = ChallengeStatus.INACTIVE;
        challenge.completeRate = 0.0;
        challenge.endDateTime = endDate;
        challenge.addChallengeUser(challengeUsers);
        return challenge;
    }

    private void addChallengeUser(ChallengeUser challengeUser) {
        challengeUsers.add(challengeUser);
        challengeUser.addChallenge(this);
    }

    public void addGathering(Gathering gathering) {
        this.gathering = gathering;
    }
}
