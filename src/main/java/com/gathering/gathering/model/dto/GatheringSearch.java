package com.gathering.gathering.model.dto;

import com.gathering.gathering.model.entity.GatheringSortType;
import com.gathering.gathering.model.entity.GatheringStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Builder
public class GatheringSearch {
    private GatheringSortType gatheringSortType;
    private Long categoryId;
    private String bookTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long goalTime;
    private GatheringStatus gatheringStatus;
}
