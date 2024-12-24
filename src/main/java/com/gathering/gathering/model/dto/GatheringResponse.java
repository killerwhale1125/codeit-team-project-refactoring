package com.gathering.gathering.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.gathering.model.entity.ReadingTimeGoal;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringStatus;
import com.gathering.user.model.entitiy.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class GatheringResponse {

    private long id;
    private String name;
    private String content;
    private int readingTimeGoal;
    private LocalDate startDate;
    private LocalDate endDate;
    private String owner;
    private int minCapacity;
    private int maxCapacity;
    private int currentCapacity;
    private GatheringStatus gatheringStatus;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String bookTitle;
    private String bookImage;
    private String publisher;
    private String publishDate;
    private double star;
    private String author;
    private double completeRate;
    private String introduce;
    private String thumbnail;
    private int gatheringWeek;
    private int bookTotalPage;
    private String userProfile;

    public static GatheringResponse fromEntity(Gathering gathering) {
        return GatheringResponse.builder()
                .id(gathering.getId())
                .owner(gathering.getOwner())
                .name(gathering.getName())
                .content(gathering.getContent())
                .thumbnail(gathering.getImage().getUrl())
                .gatheringWeek(gathering.getGatheringWeek().getWeek())
                .readingTimeGoal(gathering.getChallenge().getReadingTimeGoal().getMinutes())
                .startDate(gathering.getStartDate())
                .endDate(gathering.getEndDate())
                .minCapacity(gathering.getMinCapacity())
                .maxCapacity(gathering.getMaxCapacity())
                .currentCapacity(gathering.getCurrentCapacity())
                .gatheringStatus(gathering.getGatheringStatus())
                .createdTime(gathering.getCreatedTime())
                .updatedTime(gathering.getModifiedTime())
                .bookTitle(gathering.getBook().getTitle())
                .bookImage(gathering.getBook().getImage())
                .publisher(gathering.getBook().getPublisher())
                .publishDate(gathering.getBook().getPublishDate())
                .star(gathering.getBook().getStar())
                .author(gathering.getBook().getAuthor())
                .build();
    }

    /**
     * 마이페이지 모임 리스트 조회
     */
    public static GatheringResponse myGatheringFromEntity(Gathering gathering) {
        return GatheringResponse.builder()
                .id(gathering.getId())
                .name(gathering.getName())
                .startDate(gathering.getStartDate())
                .endDate(gathering.getEndDate())
                .currentCapacity(gathering.getCurrentCapacity())
                .completeRate(gathering.getChallenge().getCompleteRate())
                .readingTimeGoal(gathering.getChallenge().getReadingTimeGoal().getMinutes())
                .bookTitle(gathering.getBook().getTitle())
                .bookImage(gathering.getBook().getImage())
                .build();
    }

    /**
     * 모임 소개
     */
    public static GatheringResponse introduceFromEntity(Gathering gathering, User user) {
        return GatheringResponse.builder()
                .id(gathering.getId())
                .owner(gathering.getOwner())
                .userProfile(user.getProfile())
                .name(gathering.getName())
                .content(gathering.getContent())
                .readingTimeGoal(gathering.getChallenge().getReadingTimeGoal().getMinutes())
                .bookTitle(gathering.getBook().getTitle())
                .bookImage(gathering.getBook().getImage())
                .bookTotalPage(gathering.getBook().getTotalPage())
                .publisher(gathering.getBook().getPublisher())
                .publishDate(gathering.getBook().getPublishDate())
                .star(gathering.getBook().getStar())
                .author(gathering.getBook().getAuthor())
                .introduce(gathering.getBook().getIntroduce())
                .gatheringWeek(gathering.getGatheringWeek().getWeek())
                .build();
    }

    /**
     * 나의 리뷰 - 작성 가능한 모임 목록 생성자
     */
    public GatheringResponse(Long id, String name, LocalDate startDate, LocalDate endDate
            , ReadingTimeGoal readingTimeGoal, String thumbnail) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.readingTimeGoal = readingTimeGoal.getMinutes();
        this.thumbnail = thumbnail;
    }
}
