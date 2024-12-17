package com.gathering.gathering.model.dto;

import com.gathering.gathering.model.entity.GatheringWeek;
import com.gathering.challenge.model.entity.ReadingGoalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GatheringResultPageResponse {
    private Long id;
    private String name;
    private int currentCapacity;
    private int maxCapacity;
    private GatheringWeek gatheringWeek;
    private ReadingGoalTime readingGoalTime;
    private String thumbnail;
    private Long bookId;
    private String bookTitle;
    private String bookImage;
}
