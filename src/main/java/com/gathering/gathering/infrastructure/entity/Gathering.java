package com.gathering.gathering.infrastructure.entity;

import com.gathering.book.infrastructure.entity.Book;
import com.gathering.challenge.infrastructure.entity.Challenge;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.gathering.domain.GatheringStatus;
import com.gathering.gathering.domain.GatheringWeek;
import com.gathering.gathering_review.infrastructure.entity.GatheringReview;
import com.gathering.gathering_user.infrastructure.entity.GatheringUser;
import com.gathering.image.infrastructure.entity.Image;
import com.gathering.user_attendance_book.infrastructure.entity.UserAttendanceBook;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gathering.gathering.domain.GatheringStatus.ACTIVE;
import static com.gathering.gathering.domain.GatheringStatus.COMPLETED;
import static com.gathering.util.entity.EntityUtils.nullableEntity;
import static jakarta.persistence.Persistence.getPersistenceUtil;

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

//    @OneToMany(mappedBy = "gathering")
//    private List<GatheringBookReview> gatheringBookReviews = new ArrayList<>();

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

    public void start() {
        this.gatheringStatus = ACTIVE;
    }

    public void end() {
        this.gatheringStatus = COMPLETED;
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

        gatheringEntity.challenge = nullableEntity(Challenge::fromEntity, gathering.getChallenge());
        gatheringEntity.book = nullableEntity(Book::fromEntity, gathering.getBook());
        gatheringEntity.image = nullableEntity(Image::fromEntity, gathering.getImage());
        return gatheringEntity;
    }

    public GatheringDomain toEntity() {
        GatheringDomain.GatheringDomainBuilder builder = GatheringDomain.builder()
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
                .gatheringWeek(gatheringWeek);

        if (challenge != null && getPersistenceUtil().isLoaded(challenge)) {
            builder.challenge(challenge.toEntity());
        }

        if (book != null && getPersistenceUtil().isLoaded(book)) {
            builder.book(book.toEntity());
        }

        if (image != null && getPersistenceUtil().isLoaded(image)) {
            builder.image(image.toEntity());
        }

        if(gatheringUsers != null && getPersistenceUtil().isLoaded(gatheringUsers)) {
            builder.gatheringUsers(gatheringUsers.stream().map(GatheringUser::toEntity).collect(Collectors.toList()));
        }

        if(gatheringReviews != null && getPersistenceUtil().isLoaded(gatheringReviews)) {
            builder.gatheringReviews(gatheringReviews.stream().map(GatheringReview::toEntity).collect(Collectors.toList()));
        }

        return builder.build();
    }

}
