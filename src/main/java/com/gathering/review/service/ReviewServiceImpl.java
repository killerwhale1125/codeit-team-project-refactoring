package com.gathering.review.service;

import com.gathering.review.model.constant.BookReviewTagType;
import com.gathering.review.model.dto.*;
import com.gathering.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    @Override
    public ReviewListDto findReviews(BookReviewTagType tag, Pageable pageable) {
        return reviewRepository.findReviews(tag, pageable);
    }
}
