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

    @NotNull
    private long bookId;
    @NotNull
    private long gatheringId;

    @NotEmpty
    private String title;

    @NotNull
    private int appr; // 평가

    @NotEmpty
    private String tag;

    @NotEmpty
    private String content;

    private String tmprStrgYN; // 임시저장 여부

}
