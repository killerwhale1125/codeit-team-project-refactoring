package com.gathering.review.service;

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
}
