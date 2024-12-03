package com.gathering.gathering.model.dto;

import com.gathering.gathering.model.entity.GatheringSortType;
import com.gathering.gathering.model.entity.GatheringStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class GatheringSearch {
    private int page;
    private int size;
    private long categoryId;
    private String bookTitle;
    private String state;
    private String city;
    private String town;
    private LocalDate startDate;
    private LocalDate endDate;
    private long goalTime;
    private GatheringSortType gatheringSortType;
    private GatheringStatus gatheringStatus;
}
