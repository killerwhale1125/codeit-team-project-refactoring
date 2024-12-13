package com.gathering.review.service;

import com.gathering.review.model.dto.*;

import java.util.List;

public interface ReviewService {
    
    // 리뷰 생성
    ReviewDto createReview(CreateReviewDto createReviewDto, String username, String type);

    // 리뷰 댓글 생성
    ReviewCommentDto createReviewComment(CreateReviewCommentDto createReviewCommentDto, String username);

    // 나의 리뷰 목록 조회
    ReviewListDto selectUserReviewList(String username, String type);
}
