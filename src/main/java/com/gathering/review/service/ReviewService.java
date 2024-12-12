package com.gathering.review.service;

import com.gathering.review.model.dto.CreateReviewCommentDto;
import com.gathering.review.model.dto.CreateReviewDto;
import com.gathering.review.model.dto.ReviewDto;

public interface ReviewService {
    
    // 리뷰 생성
    ReviewDto createReview(CreateReviewDto createReviewDto, String username, String type);

    // 리뷰 댓글 생성
    ReviewDto createReviewComment(CreateReviewCommentDto createReviewCommentDto, String username);
}
