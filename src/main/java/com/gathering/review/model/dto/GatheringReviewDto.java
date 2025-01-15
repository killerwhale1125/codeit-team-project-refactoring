package com.gathering.review.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.review.domain.StatusType;
import com.gathering.gathering_review.infrastructure.entity.GatheringReview;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    private StatusType status;
    private String createDate;
    private String userName;
    private String profile;


    /**
     * 리뷰 목록 생성자
     */
    public GatheringReviewDto(long id,long userId, String userName, String profile,int score, String content, String createDate) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.profile = profile;
        this.content = content;
        this.score = score;
        this.createDate = createDate;
    }

    /**
     * 나의 리뷰 목록 생성자
     */
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
