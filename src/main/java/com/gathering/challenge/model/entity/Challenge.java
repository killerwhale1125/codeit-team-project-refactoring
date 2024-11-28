package com.gathering.challenge.model.entity;

import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.model.entity.Gathering;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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

    public static Challenge create(ChallengeUser challengeUser, LocalDateTime endDate) {
        Challenge challenge = new Challenge();
        challenge.getChallengeUsers().add(challengeUser);
        challenge.challengeStatus = ChallengeStatus.INACTIVE;
        challenge.completeRate = 0.0;
        challenge.endDateTime = endDate;
        return challenge;
    }
}
