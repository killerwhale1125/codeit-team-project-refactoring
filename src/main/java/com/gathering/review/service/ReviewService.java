package com.gathering.review.service;

import com.gathering.book.model.dto.BookResponse;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.gathering.model.entity.SearchType;
import com.gathering.review.model.constant.BookReviewTagType;
import com.gathering.review.model.constant.ReviewType;
import com.gathering.review.model.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    
    // 리뷰 생성
    ReviewDto createReview(CreateReviewDto createReviewDto, String username, ReviewType type);

    // 리뷰 댓글 생성
    ReviewCommentDto createReviewComment(CreateReviewCommentDto createReviewCommentDto, String username);

    // 나의 리뷰 목록 조회
    ReviewListDto selectUserReviewList(String username, ReviewType type, Pageable pageable);

    // 독서 리뷰 페이지 정보 조회
    ReviewListDto selectBookReviewList(String username);

    // 리뷰 목록 조회(무한 스크롤)
    ReviewListDto findReviews(BookReviewTagType tag, Pageable pageable, String username);

    // 리뷰 상세 조회
    ReviewDto selectBookReviewDetail(long reviewId, String username);

    // 리뷰 검색 (상세 페이지 사이드바 및 통합 검색)
    ReviewListDto searchReviews(SearchType type, String searchParam, Pageable pageable, String username);

    int DeleteReview(long reviewId,ReviewType type, String username);

    // 리뷰 업데이트
    void UpdateReview(CreateReviewDto createReviewDto, long reviewId, ReviewType type, String username);

    // 리뷰 좋아요
    void UpdateReviewLike(ReviewLikeDto reviewLikeDto, String username);

    // 댓글 삭제
    int DeleteComment(long commentId, String username);

    // 댓글 수정
    int UpdateComment(long commentId, CreateReviewCommentDto updateReviewCommentDto, String username);

    // 내가 참여한 모임 책 목록
    List<BookResponse> searchUserGatheringBooks(String username);

    // 추천 검색어( 독서 리뷰 페이지의 bestReview 목록에서 아이디랑 책 제목만 추출)
    List<BookResponse> getRecommendedKeywords();
}
