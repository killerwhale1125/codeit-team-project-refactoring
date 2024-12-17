package com.gathering.gathering.model.dto;

import com.gathering.gathering.model.entity.GatheringWeek;
import com.gathering.gathering.model.entity.ReadingTimeGoal;
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
    private ReadingTimeGoal readingTimeGoal;
    private String thumbnail;
    private String bookTitle;
    private String bookImage;
}
