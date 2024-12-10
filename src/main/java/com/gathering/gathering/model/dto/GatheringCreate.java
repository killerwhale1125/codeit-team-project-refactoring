package com.gathering.gathering.model.dto;

import com.gathering.gathering.model.entity.GatheringAddress;
import com.gathering.gathering.model.entity.GatheringStatus;
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
    private LocalDate endDate;  // 모임 종료일
    @NotNull
    private LocalDate startDate;    // 모임 시작일 ( 생성일 X, 챌린지가 시작되는 날짜 )
    @Min(1)  // 최소 1명 이상
    private int minCapacity;
    @Min(1)  // 최소 1명 이상
    private int maxCapacity;
    @NotNull
    private Long bookId;
    @NotNull
    private Long categoryId;
    @NotNull
    private GatheringStatus gatheringStatus;
}
