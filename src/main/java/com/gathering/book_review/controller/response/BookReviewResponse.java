package com.gathering.book_review.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.review.domain.StatusType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookReviewResponse {
    private long id;

    private long userId;

    private long bookId;

    private String title;

    private String apprCd;

    private String tagCd;

    private String content;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    private int likes;

    @Enumerated(EnumType.STRING)
    private StatusType status;

    private String createTime;

    // best 리뷰 파라미터
    private long commentCnt;
    private long writerReviewCnt;
    private String userName;
    private String profile;

    // 리뷰 목록 무한스크롤 파라미터
    private String bookImage;

    // 현재 사용자가 좋아요를 눌렀는지 여부
    private boolean userLikeCk;

    // 통합 검색
    private String bookTitle;

    public static BookReviewResponse fromEntity(BookReviewDomain bookReview) {
        return BookReviewResponse.builder()
                .id(bookReview.getId())
                .userId(bookReview.getUser().getId())
                .bookId(bookReview.getBook().getId())
                .title(bookReview.getTitle())
                .apprCd(bookReview.getApprCd())
                .tagCd(bookReview.getTagCd())
                .content(bookReview.getContent())
                .likes(bookReview.getLikes())
                .status(bookReview.getStatus())
                .build();
    }
}
