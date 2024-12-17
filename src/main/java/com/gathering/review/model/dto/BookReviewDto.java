package com.gathering.review.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.review.model.constant.StatusType;
import com.gathering.review.model.entitiy.BookReview;
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
public class BookReviewDto extends ReviewDto {

    private long id;

    private long userId;

    private long bookId;

    private long gatheringId;

    private String title;

    private String apprCd;

    private String tag;

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

    public BookReviewDto(long id, String title, String createTime,String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }

    // best 리뷰 생성자
    public BookReviewDto(long id, String title, String content, int likes
    ,long commentCnt, long writerReviewCnt, String userName, String profile) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.commentCnt = commentCnt;
        this.writerReviewCnt = writerReviewCnt;
        this.userName = userName;
        this.profile = profile;
    }

    public BookReviewDto(long id, String title, String apprCd, String bookImage, String content,
                         int likes, long commentCnt, long userId, String profile, String userName, String createTime) {
        this.id = id;
        this.title = title;
        this.apprCd = apprCd;
        this.bookImage = bookImage;
        this.content = content;
        this.likes = likes;
        this.commentCnt = commentCnt;
        this.userId = userId;
        this.profile = profile;
        this.userName = userName;
        this.createTime = createTime;
    }
    // 리뷰 상세페이지 사이드에 특정 책에 대한 리뷰 목록 or 통합 검색 결과 생성자
    public BookReviewDto(long id, String title,String content, int likes,long commentCnt,
                         long userId, String profile, String userName, String createTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.commentCnt = commentCnt;
        this.userId = userId;
        this.profile = profile;
        this.userName = userName;
        this.createTime = createTime;
    }
    public static BookReviewDto fromEntity(BookReview review) {
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
