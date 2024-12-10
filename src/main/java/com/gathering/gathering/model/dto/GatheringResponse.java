package com.gathering.gathering.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GatheringResponse {

    private Long id;
    private String name;
    private String content;
    private Long goalDays;
    private Integer readingTimeGoal;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer minCapacity;
    private Integer maxCapacity;
    private GatheringStatus gatheringStatus;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String bookTitle;
    private String bookImage;
    private String publisher;
    private String publishDate;
    private Double star;

    public static GatheringResponse fromEntity(Gathering gathering) {
        return GatheringResponse.builder()
                .id(gathering.getId())
                .name(gathering.getName())
                .content(gathering.getContent())
                .goalDays(gathering.getGoalDays())
                .readingTimeGoal(gathering.getChallenge().getReadingTimeGoal().getMinutes())
                .startDate(gathering.getStartDate())
                .endDate(gathering.getEndDate())
                .minCapacity(gathering.getMinCapacity())
                .maxCapacity(gathering.getMaxCapacity())
                .gatheringStatus(gathering.getGatheringStatus())
                .createdTime(gathering.getCreatedTime())
                .updatedTime(gathering.getModifiedTime())
                .bookTitle(gathering.getBook().getTitle())
                .bookImage(gathering.getBook().getImage())
                .publisher(gathering.getBook().getPublisher())
                .publishDate(gathering.getBook().getPublishDate())
                .star(gathering.getBook().getStar())
                .build();
    }

    public static GatheringResponse myGatheringFromEntity(Gathering gathering) {
        return GatheringResponse.builder()
                .name(gathering.getName())
                .startDate(gathering.getStartDate())
                .endDate(gathering.getEndDate())
                .build();
    }
}
