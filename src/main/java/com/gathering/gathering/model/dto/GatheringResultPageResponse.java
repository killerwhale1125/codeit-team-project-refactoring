package com.gathering.gathering.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GatheringResultPageResponse {
    private Long id;
    private String name;
    private int currentCapacity;
    private int maxCapacity;
    private int gatheringWeek;
    private int readingTimeGoal;
    private String thumbnail;
    private Long bookId;
    private String bookTitle;
    private String bookImage;
}
