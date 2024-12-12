package com.gathering.review.model.dto;

import com.gathering.review.model.entitiy.BookReview;
import com.gathering.review.model.entitiy.GatheringReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ReviewDto {

    private long id;

    private long userId;

    private long bookId;

    private long gatheringId;

    private String title;

    private String apprCd;

    private String tag;

    private String content;

    private int likes;

    private int score;

    private String status;



    public static ReviewDto formEntity(BookReview review) {
        return ReviewDto.builder()
                .id(review.getId())
                .userId(review.getUser().getId())
                .bookId(review.getBook().getId())
                .title(review.getTitle())
                .apprCd(review.getApprCd())
                .tag(review.getTagCd())
                .content(review.getContent())
                .likes(review.getLikes())
                .status(review.getStatus())
                .build();
    }

    public static ReviewDto formEntity(GatheringReview review) {
        return ReviewDto.builder()
                .id(review.getId())
                .userId(review.getUser().getId())
                .gatheringId(review.getGathering().getId())
                .content(review.getContent())
                .score(review.getScore())
                .status(review.getStatus())
                .build();
    }
}
