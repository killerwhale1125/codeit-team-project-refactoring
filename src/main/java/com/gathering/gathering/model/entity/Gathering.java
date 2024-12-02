package com.gathering.gathering.model.entity;

import com.gathering.book.model.entity.Book;
import com.gathering.challenge.model.entity.Challenge;
import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.util.holder.DateCalculateHolder;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.gathering.common.base.response.BaseResponseStatus.*;

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
    private LocalDate endDateTime;
    private long goalDays;
    private int maxCapacity;
    private int minCapacity;
    private int currentCapacity;
    private long ownerId;

    @Embedded
    private GatheringAddress gatheringAddress;

    @Enumerated(EnumType.STRING)
    private GatheringStatus gatheringStatus;

    @OneToMany(mappedBy = "gathering", cascade = CascadeType.ALL)
    private List<GatheringUser> gatheringUsers = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_Id")
    private Book book;

    public static Gathering createGathering(GatheringCreate gatheringCreate, Challenge challenge, Book book, GatheringUser gatheringUser, DateCalculateHolder dateCalculateHolder) {
        Gathering gathering = new Gathering();
        gathering.name = gatheringCreate.getName();
        gathering.gatheringAddress = gatheringCreate.toAddress();
        gathering.content = gatheringCreate.getContent();
        gathering.endDateTime = gatheringCreate.getEndDate();
        gathering.goalDays = dateCalculateHolder.calculateGoalDays(gatheringCreate.getGatheringDate(), gatheringCreate.getEndDate());
        validateCapacity(gatheringCreate.getMinCapacity(), gatheringCreate.getMaxCapacity());
        gathering.maxCapacity = gatheringCreate.getMaxCapacity();
        gathering.minCapacity = gatheringCreate.getMinCapacity();
        gathering.gatheringStatus = GatheringStatus.RECRUITING;
        gathering.book = book;
        gathering.currentCapacity = 1;
        gathering.ownerId = gatheringUser.getUser().getId();
        gathering.addChallenge(challenge);
        gathering.addGatheringUser(gatheringUser);
        return gathering;
    }

    public static void validateCapacity(int minCapacity, int maxCapacity) {
        if (minCapacity <= 0) throw new BaseException(INVALID_MIN_CAPACITY);
        if (maxCapacity <= 0) throw new BaseException(INVALID_MAX_CAPACITY);
        if (maxCapacity < minCapacity) throw new BaseException(INVALID_CAPACITY_RANGE);
    }

    public void addGatheringUser(GatheringUser gatheringUser) {
        // 인원 수 검사
        if(validateCapacityLimit()) throw new BaseException(EXCEEDS_CAPACITY);

        gatheringUsers.add(gatheringUser);
        gatheringUser.addGathering(this);
    }

    public void addChallenge(Challenge challenge) {
        this.challenge = challenge;
        challenge.addGathering(this);
    }
    
    // 모집 인원 제한 검증
    public boolean validateCapacityLimit() {
        return this.currentCapacity + 1 > maxCapacity ? false : true;
    }

    // 유저가 모임에 참여중인지 검증
    public void isUserAlreadyJoined(long userId) {
        if(gatheringUsers.stream()
                .anyMatch(gatheringUser -> gatheringUser.getUser().getId() == userId)) {
            throw new BaseException(ALREADY_JOINED);
        }
    }

    public void join(long userId, GatheringUser gatheringUser) {
        // 이미 참여한 유저인지 검증
        isUserAlreadyJoined(userId);
        addGatheringUser(gatheringUser);
    }

    public void validateOwner(Long userId) {
        if(this.ownerId != userId) throw new BaseException(ACCESS_DENIED);
    }
}
