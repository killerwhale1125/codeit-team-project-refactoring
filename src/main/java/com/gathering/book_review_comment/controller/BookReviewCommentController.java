package com.gathering.book_review_comment.controller;

import com.gathering.book_review_comment.controller.port.BookReviewCommentService;
import com.gathering.book_review_comment.controller.response.BookReviewCommentResponse;
import com.gathering.book_review_comment.domain.BookReviewCommentCreate;
import com.gathering.common.base.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book/{bookId}/review-comment")
public class BookReviewCommentController {

    private final BookReviewCommentService bookReviewCommentService;

    public BaseResponse<BookReviewCommentResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid BookReviewCommentCreate bookReviewCommentCreate) {
        return new BaseResponse<>(bookReviewCommentService.create(userDetails.getUsername(), bookReviewCommentCreate));
    }
}
