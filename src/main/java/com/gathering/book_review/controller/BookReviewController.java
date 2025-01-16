package com.gathering.book_review.controller;

import com.gathering.book_review.controller.port.BookReviewService;
import com.gathering.book_review.controller.response.BookReviewResponse;
import com.gathering.book_review.domain.BookReviewCreate;
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
@RequestMapping("/api/book/{bookId}/reviews")
public class BookReviewController {

    private final BookReviewService bookReviewService;

    @PostMapping
    public BaseResponse<BookReviewResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BookReviewCreate bookReviewCreate) {
        return new BaseResponse<>(bookReviewService.create(userDetails.getUsername(), bookReviewCreate));
    }


}
