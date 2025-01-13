package com.gathering.gathering.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class GatheringSearch {
    private GatheringSortType gatheringSortType;
    private Long categoryId;
    private String bookTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private GatheringStatus gatheringStatus;
    private List<ReadingTimeGoal> readingTimeGoals;
    private Boolean today;
}
