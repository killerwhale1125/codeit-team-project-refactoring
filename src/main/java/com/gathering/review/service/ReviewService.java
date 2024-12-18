package com.gathering.review.service;

import com.gathering.gathering.model.entity.SearchType;
import com.gathering.review.model.constant.BookReviewTagType;
import com.gathering.review.model.dto.*;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    
    // 리뷰 생성
    ReviewDto createReview(CreateReviewDto createReviewDto, String username, String type);

    // 리뷰 댓글 생성
    ReviewCommentDto createReviewComment(CreateReviewCommentDto createReviewCommentDto, String username);

    // 나의 리뷰 목록 조회
    ReviewListDto selectUserReviewList(String username, String type);

    // 독서 리뷰 페이지 정보 조회
    ReviewListDto selectBookReviewList(String username);

    // 리뷰 목록 조회(무한 스크롤)
    ReviewListDto findReviews(BookReviewTagType tag, Pageable pageable);

    // 리뷰 상세 조회
    ReviewDto selectBookReviewDetail(long reviewId);

    // 리뷰 검색 (상세 페이지 사이드바 및 통합 검색)
    ReviewListDto searchReviews(SearchType type, String searchParam, Pageable pageable);
}
