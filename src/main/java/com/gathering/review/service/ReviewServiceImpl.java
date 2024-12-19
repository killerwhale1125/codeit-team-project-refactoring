package com.gathering.review.service;

import com.gathering.gathering.model.entity.SearchType;
import com.gathering.review.model.constant.BookReviewTagType;
import com.gathering.review.model.constant.ReviewType;
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
    public ReviewDto createReview(CreateReviewDto createReviewDto, String username, ReviewType type) {
        return reviewRepository.createReview(createReviewDto, username, type);
    }

    @Override
    public ReviewCommentDto createReviewComment(CreateReviewCommentDto createReviewCommentDto, String username) {
        return reviewRepository.createReviewComment(createReviewCommentDto, username);
    }

    @Override
    public ReviewListDto selectUserReviewList(String username, ReviewType type, Pageable pageable) {
        return reviewRepository.selectUserReviewList(username, type, pageable);
    }

    @Override
    public ReviewListDto selectBookReviewList(String username) {
        return reviewRepository.selectBookReviewList(username);
    }

    @Override
    public ReviewListDto findReviews(BookReviewTagType tag, Pageable pageable) {
        return reviewRepository.findReviews(tag, pageable);
    }

    @Override
    public ReviewDto selectBookReviewDetail(long reviewId) {
        return reviewRepository.selectBookReviewDetail(reviewId);
    }

    @Override
    public ReviewListDto searchReviews(SearchType type, String searchParam, Pageable pageable) {
        return reviewRepository.searchReviews(type, searchParam, pageable);
    }

    @Override
    public int DeleteBookReview(long reviewId,ReviewType type, String username) {
        return reviewRepository.DeleteBookReview(reviewId,type, username);
    }
}
