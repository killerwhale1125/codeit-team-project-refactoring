package com.gathering.review.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.review.model.entitiy.BookReview;
import com.gathering.review.model.entitiy.GatheringReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Builder
public class GatheringReviewDto extends ReviewDto {

    private long id;

    private long userId;

    private long gatheringId;

    private String content;

    private int score;

    private String status;

    private String createDate;

    public GatheringReviewDto(long id,int score, String content, String createDate) {
        this.id = id;
        this.content = content;
        this.score = score;
        this.createDate = createDate;
    }

    public static GatheringReviewDto formEntity(GatheringReview review) {
        return GatheringReviewDto.builder()
                .id(review.getId())
                .userId(review.getUser().getId())
                .gatheringId(review.getGathering().getId())
                .content(review.getContent())
                .score(review.getScore())
                .status(review.getStatus())
                .build();
    }
}
