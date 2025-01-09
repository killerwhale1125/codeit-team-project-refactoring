package com.gathering.gathering.model.entity;

import com.gathering.book.model.entity.Book;
import com.gathering.challenge.model.entity.Challenge;
import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.model.domain.GatheringDomain;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.validator.GatheringValidator;
import com.gathering.gatheringuser.model.entity.GatheringUser;
import com.gathering.image.model.entity.Image;
import com.gathering.review.model.entitiy.GatheringReview;
import com.gathering.user.model.domain.UserDomain;
import com.gathering.user.model.entitiy.UserAttendanceBook;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gathering.common.base.response.BaseResponseStatus.*;
import static com.gathering.gathering.model.entity.GatheringStatus.*;

@Getter
@Entity
public class Gathering extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gathering_id")
    private Long id;

    private String name;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private int maxCapacity;
    private int minCapacity;
    private int currentCapacity = 0;
    private String owner;
    private long viewCount;

    @Enumerated(EnumType.STRING)
    private GatheringStatus gatheringStatus;

    @OneToMany(mappedBy = "gathering")
    private List<GatheringUser> gatheringUsers = new ArrayList<>();

    @OneToMany(mappedBy = "gathering")
    private List<GatheringBookReview> gatheringBookReviews = new ArrayList<>();

    @OneToMany(mappedBy = "gathering")
    private List<GatheringReview> gatheringReviews = new ArrayList<>();

    @OneToMany(mappedBy = "gathering")
    private List<UserAttendanceBook> userAttendanceBooks = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_Id")
    private Book book;

    @Enumerated(EnumType.STRING)
    private GatheringWeek gatheringWeek;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    // 모임 떠나기
    public static void leave(Gathering gathering, UserDomain user) {
        List<GatheringUser> gatheringUsers = gathering.getGatheringUsers();
        GatheringUser gatheringUser = gatheringUsers.stream()
                .filter(gu -> gu.getUser().getId() == user.getId())
                .findFirst()
                .orElseThrow(() -> new BaseException(USER_NOT_IN_GATHERING));

        if(gatheringUser.getUser().getUserName() == gathering.getOwner())
            throw new BaseException(HOST_CANNOT_LEAVE_GATHERING);

        // 모임의 참여자 리스트에서 유저 제거
        gatheringUsers.remove(gatheringUser);
        // 현재 인원 수 감소
        gathering.decreaseCurrentCapacity();
        // 정원이 채워졌다면 모집 중으로 변경)
        gathering.checkIsFullAndUpdateStatus();
    }

    public static Gathering fromEntity(GatheringDomain gathering) {
        Gathering gatheringEntity = new Gathering();
        gatheringEntity.id = gathering.getId();
        gatheringEntity.name = gathering.getName();
        gatheringEntity.content = gathering.getContent();
        gatheringEntity.startDate = gathering.getStartDate();
        gatheringEntity.endDate = gathering.getEndDate();
        gatheringEntity.minCapacity = gathering.getMinCapacity();
        gatheringEntity.maxCapacity = gathering.getMaxCapacity();
        gatheringEntity.currentCapacity = gathering.getCurrentCapacity();
        gatheringEntity.owner = gathering.getOwner();
        gatheringEntity.viewCount = gathering.getViewCount();
        gatheringEntity.gatheringStatus = gathering.getGatheringStatus();
        gatheringEntity.gatheringWeek = gathering.getGatheringWeek();
        gatheringEntity.challenge = Challenge.fromEntity(gathering.getChallenge());
        gatheringEntity.book = Book.fromEntity(gathering.getBook());
        gatheringEntity.image = Image.fromEntity(gathering.getImage());
        return gatheringEntity;
    }

    // 모임 유저 추가
    private void addGatheringUser(GatheringUser gatheringUser, GatheringValidator gatheringValidator) {
        gatheringValidator.validateCapacityLimit(this.currentCapacity, maxCapacity);
        gatheringUsers.add(gatheringUser);
        gatheringUser.addGathering(this);
    }

    // 모임 인원 수 감소
    private void decreaseCurrentCapacity() {
        this.currentCapacity--;
    }

    // 모임 상태 변화
    private void checkIsFullAndUpdateStatus() {
        if (this.currentCapacity >= this.maxCapacity) {
            this.gatheringStatus = FULL;
        }

        if (this.currentCapacity < this.maxCapacity && this.gatheringStatus == FULL) {
            this.gatheringStatus = RECRUITING;
        }
    }

    public void start() {
        this.gatheringStatus = ACTIVE;
    }

    public void end() {
        this.gatheringStatus = COMPLETED;
        for(GatheringUser gatheringUser : gatheringUsers) {
            gatheringUser.endGatheringStatus();
        }
    }

    public GatheringDomain toEntity() {
        return GatheringDomain.builder()
                .id(id)
                .name(name)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .minCapacity(minCapacity)
                .maxCapacity(maxCapacity)
                .currentCapacity(currentCapacity)
                .owner(owner)
                .viewCount(viewCount)
                .gatheringStatus(gatheringStatus)
                .challenge(challenge.toEntity())
                .book(book.toEntity())
                .gatheringWeek(gatheringWeek)
                .image(image.toEntity())
                .gatheringUsers(gatheringUsers.stream().map(GatheringUser::toEntity).collect(Collectors.toList()))
                .build();
    }

    public void addGatheringUser(GatheringUser gatheringUserEntity) {
        gatheringUsers.add(gatheringUserEntity);
    }
}
