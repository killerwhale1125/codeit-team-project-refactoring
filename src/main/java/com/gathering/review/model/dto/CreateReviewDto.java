package com.gathering.review.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateReviewDto {

    private long bookId;
    private long gatheringId;
    private String title;
    private String apprCd; // 평가
    private String tag;
    @NotEmpty
    private String content;
    private int score;
    private String tmprStrgYN; // 임시저장 여부

}
