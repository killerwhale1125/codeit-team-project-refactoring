package com.gathering.review.repository;

import com.gathering.review.model.dto.*;

import java.util.List;

public interface ReviewRepository {
    ReviewDto createReview(CreateReviewDto createReviewDto, String username, String type);

    ReviewCommentDto createReviewComment(CreateReviewCommentDto createReviewCommentDto, String username);

    ReviewListDto selectUserReviewList(String username, String type);

    ReviewListDto selectBookReviewList(String username);
}
