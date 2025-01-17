package com.gathering.book_review_like.controller;

import com.gathering.book_review_like.controller.port.BookReviewLikeService;
import com.gathering.book_review_like.domain.BookReviewLikeCreate;
import com.gathering.common.base.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book-review-like")
public class BookReviewLikeController {

    private final BookReviewLikeService bookReviewLikeService;

    /* 독서 리뷰 좋아요 */
    @PostMapping
    public BaseResponse<Void> like(
            @RequestBody BookReviewLikeCreate bookReviewLikeCreate,
            @AuthenticationPrincipal UserDetails userDetails) {
        bookReviewLikeService.like(bookReviewLikeCreate, userDetails.getUsername());
        return new BaseResponse<>();
    }
}
