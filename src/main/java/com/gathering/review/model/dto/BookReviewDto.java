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

    // 통학 검색
    private String bookTitle;

    
    // 나의 리뷰 목록 조회
    public BookReviewDto(long id, String title, String content,String createTime,int likes, long commentCnt, boolean userLikeCk ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.likes = likes;
        this.commentCnt = commentCnt;
        this.userLikeCk = userLikeCk;
    }

    // best 리뷰 생성자 (비회원)
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

    // best 리뷰 생성자
    public BookReviewDto(long id, String title, String content, int likes
            ,long commentCnt, long writerReviewCnt, String userName, String profile, boolean userLikeCk) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.commentCnt = commentCnt;
        this.writerReviewCnt = writerReviewCnt;
        this.userName = userName;
        this.profile = profile;
        this.userLikeCk = userLikeCk;
    }

    // 독서 리뷰 무한스크롤 생성자(비회원)
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

    // 독서 리뷰 무한스크롤 생성자
    public BookReviewDto(long id, String title, String apprCd, String bookImage, String content,
                         int likes, long commentCnt, long userId, String profile, String userName, String createTime, boolean userLikeCk) {
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
        this.userLikeCk = userLikeCk;
    }

    // 독서 리뷰 상세 정보 (비회원)
    public BookReviewDto(long id,long bookId, String title, String apprCd,String tagCd, String content,
                         int likes, long userId, String profile, String userName,long writerReviewCnt, String createTime,
                         long gatheringId) {
        this.id = id;
        this.bookId = bookId;
        this.title = title;
        this.apprCd = apprCd;
        this.tagCd = tagCd;
        this.content = content;
        this.likes = likes;
        this.userId = userId;
        this.profile = profile;
        this.userName = userName;
        this.writerReviewCnt = writerReviewCnt;
        this.createTime = createTime;
        this.gatheringId = gatheringId;
    }
    // 독서 리뷰 상세 정보
    public BookReviewDto(long id,long bookId, String title, String apprCd,String tagCd, String content,
                         int likes, long userId, String profile, String userName,long writerReviewCnt, String createTime, boolean userLikeCk,
                         long gatheringId) {
        this.id = id;
        this.bookId = bookId;
        this.title = title;
        this.apprCd = apprCd;
        this.tagCd = tagCd;
        this.content = content;
        this.likes = likes;
        this.userId = userId;
        this.profile = profile;
        this.userName = userName;
        this.writerReviewCnt = writerReviewCnt;
        this.createTime = createTime;
        this.userLikeCk = userLikeCk;
        this.gatheringId = gatheringId;
    }
    // 리뷰 상세페이지 사이드에 특정 책에 대한 리뷰 목록 or 통합 검색 결과 생성자 (비회원)
    public BookReviewDto(long id, String title,String content, int likes,long commentCnt,
                         long userId, String profile, String userName, String createTime, String bookTitle) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.commentCnt = commentCnt;
        this.userId = userId;
        this.profile = profile;
        this.userName = userName;
        this.createTime = createTime;
        this.bookTitle = bookTitle;
    }

    // 리뷰 상세페이지 사이드에 특정 책에 대한 리뷰 목록 or 통합 검색 결과 생성자
    public BookReviewDto(long id, String title,String content, int likes,long commentCnt,
                         long userId, String profile, String userName, String createTime, boolean userLikeCk, String bookTitle) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.commentCnt = commentCnt;
        this.userId = userId;
        this.profile = profile;
        this.userName = userName;
        this.createTime = createTime;
        this.userLikeCk = userLikeCk;
        this.bookTitle = bookTitle;
    }
    public static BookReviewDto fromEntity(BookReview review) {
        return BookReviewDto.builder()
                .id(review.getId())
                .userId(review.getUser().getId())
                .bookId(review.getBook().getId())
                .title(review.getTitle())
                .apprCd(review.getApprCd())
                .tagCd(review.getTagCd())
                .content(review.getContent())
                .likes(review.getLikes())
                .status(review.getStatus())
                .build();
    }
}
