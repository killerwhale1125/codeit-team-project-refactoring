package com.gathering.gathering.model.entity;

import com.gathering.challenge.model.entity.Challenge;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.util.holder.DurationHolder;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gathering extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gathering_id")
    private Long id;

    private String name;
    private String content;
    private LocalDateTime endDateTime;
    private long goalDays;
    private int maxCapacity;
    private int minCapacity;

    @Enumerated(EnumType.STRING)
    private GatheringType gatheringType;

    @Embedded
    private GatheringAddress gatheringAddress;

    @Enumerated
    private GatheringStatus gatheringStatus;

    @OneToMany(mappedBy = "gathering", cascade = CascadeType.ALL)
    private List<GatheringUser> gatheringUsers = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    public static Gathering createGathering(GatheringCreate gatheringCreate, Challenge challenge, GatheringUser gatheringUser, DurationHolder durationHolder) {
        Gathering gathering = new Gathering();
        gathering.name = gatheringCreate.getName();
        gathering.gatheringType = gatheringCreate.getGatheringType();
        gathering.gatheringAddress = gatheringCreate.toAddress();
        gathering.content = gatheringCreate.getContent();
        gathering.endDateTime = gatheringCreate.getEndDate();
        gathering.goalDays = durationHolder.calculateGoalDays(gatheringCreate.getGatheringDate(), gatheringCreate.getEndDate());
        gathering.maxCapacity = gathering.getMaxCapacity();
        gathering.minCapacity = gathering.getMinCapacity();
        gathering.gatheringStatus = GatheringStatus.RECRUITING;
        gathering.addChallenge(challenge);
        gathering.addGatheringUser(gatheringUser);
        return gathering;
    }

    private void addGatheringUser(GatheringUser gatheringUser) {
        gatheringUsers.add(gatheringUser);
        gatheringUser.addGathering(this);
    }

    public void addChallenge(Challenge challenge) {
        this.challenge = challenge;
        challenge.addGathering(this);
    }
}
