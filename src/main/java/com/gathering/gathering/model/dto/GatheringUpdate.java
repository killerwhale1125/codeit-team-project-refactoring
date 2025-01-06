package com.gathering.gathering.model.dto;

import com.gathering.gathering.model.entity.GatheringStatus;
import com.gathering.gathering.model.entity.ReadingTimeGoal;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GatheringUpdate {
    private String name;    // 제목
    private String content;
    private LocalDate endDate;  // 모임 종료일
    private LocalDate startDate;    // 모임 시작일 ( 생성일 X, 챌린지가 시작되는 날짜 )
    private int minCapacity;
    private int maxCapacity;
    private Long bookId;
    private Long categoryId;
    private GatheringStatus gatheringStatus;
    private ReadingTimeGoal readingTimeGoal;
}
