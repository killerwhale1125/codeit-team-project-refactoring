package com.gathering.gathering.controller.response;

import com.gathering.gathering.domain.GatheringStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class GatheringResultPageResponse {
    private Long id;
    private String name;
    private int currentCapacity;
    private int maxCapacity;
    private int gatheringWeek;
    private GatheringStatus gatheringStatus;
    private Date startDate;
    private int readingTimeGoal;
    private String thumbnail;
    private Long bookId;
    private String bookTitle;
    private String bookImage;
}
