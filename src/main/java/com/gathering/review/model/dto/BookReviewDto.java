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
public class BookReviewDto extends ReviewDto {

    private long id;

    private long userId;

    private long bookId;

    private long gatheringId;

    private String title;

    private String apprCd;

    private String tag;

    private String content;

    private int likes;

    private String status;

    private String createTime;

    public BookReviewDto(long id, String title, String createTime,String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }

    public static BookReviewDto formEntity(BookReview review) {
        return BookReviewDto.builder()
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
}
