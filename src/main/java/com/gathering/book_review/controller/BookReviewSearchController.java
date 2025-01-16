package com.gathering.book_review.controller;

import com.gathering.book_review.controller.port.BookReviewSearchService;
import com.gathering.book_review.controller.response.BookReviewsResponse;
import com.gathering.common.base.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book-reviewSearch")
public class BookReviewSearchController {

    private final BookReviewSearchService bookReviewSearchService;

    @GetMapping("/my-reviews")
    public BaseResponse<BookReviewsResponse> myReviews(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam int page, @RequestParam int size) {
        return new BaseResponse<>(bookReviewSearchService.myReviews(userDetails.getUsername(), page, size));
    }
}
