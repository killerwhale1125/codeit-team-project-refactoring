package com.gathering.gathering.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GatheringCreate {
    @NotEmpty
    private String name;    // 제목
    @NotEmpty
    private String content;
    @NotNull
    private LocalDate endDate;  // 챌린지 종료일
    @NotNull
    private LocalDate startDate;    // 챌린지 시작일 (모집 마감일과 같음)
    @Min(5)  // 최소 5명 이상
    private int minCapacity;
    @Min(6)  // 최소 6명 이상
    private int maxCapacity;
    private int currentCapacity;
    @NotNull
    private Long bookId;
    @NotNull
    private GatheringStatus gatheringStatus;
    @NotNull
    private ReadingTimeGoal readingTimeGoal;
    @NotNull
    private GatheringWeek gatheringWeek;
}
