package com.gathering.review.repository;

import com.gathering.review.model.dto.CreateReviewCommentDto;
import com.gathering.review.model.dto.CreateReviewDto;
import com.gathering.review.model.dto.ReviewDto;

public interface ReviewRepository {
    ReviewDto createReview(CreateReviewDto createReviewDto, String username, String type);

    ReviewDto createReviewComment(CreateReviewCommentDto createReviewCommentDto, String username);
}
