package com.gathering.review.service;

import com.gathering.review.model.dto.CreateReviewCommentDto;
import com.gathering.review.model.dto.CreateReviewDto;
import com.gathering.review.model.dto.ReviewDto;
import com.gathering.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;

    @Override
    public ReviewDto createReview(CreateReviewDto createReviewDto, String username, String type) {
        return reviewRepository.createReview(createReviewDto, username, type);
    }

    @Override
    public ReviewDto createReviewComment(CreateReviewCommentDto createReviewCommentDto, String username) {
        return reviewRepository.createReviewComment(createReviewCommentDto, username);
    }
}
