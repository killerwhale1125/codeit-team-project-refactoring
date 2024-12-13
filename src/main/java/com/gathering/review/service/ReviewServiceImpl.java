package com.gathering.review.service;

import com.gathering.review.model.dto.*;
import com.gathering.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;

    @Override
    public ReviewDto createReview(CreateReviewDto createReviewDto, String username, String type) {
        return reviewRepository.createReview(createReviewDto, username, type);
    }

    @Override
    public ReviewCommentDto createReviewComment(CreateReviewCommentDto createReviewCommentDto, String username) {
        return reviewRepository.createReviewComment(createReviewCommentDto, username);
    }

    @Override
    public ReviewListDto selectUserReviewList(String username, String type) {
        return reviewRepository.selectUserReviewList(username, type);
    }

    @Override
    public ReviewListDto selectBookReviewList(String username) {
        return reviewRepository.selectBookReviewList(username);
    }
}
