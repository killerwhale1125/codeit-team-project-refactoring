package com.gathering.book_review.controller;

import com.gathering.book_review.controller.port.BookReviewService;
import com.gathering.book_review.controller.response.BookReviewResponse;
import com.gathering.book_review.controller.response.BookReviewsResponse;
import com.gathering.book_review.domain.BookReviewCreate;
import com.gathering.book_review.domain.BookReviewUpdate;
import com.gathering.common.base.response.BaseResponse;
import com.gathering.gathering.domain.GatheringStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book-review")
public class BookReviewController {

    private final BookReviewService bookReviewService;

    /* 독서 리뷰 생성 */
    @PostMapping
    public BaseResponse<BookReviewResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BookReviewCreate bookReviewCreate) {
        return new BaseResponse<>(bookReviewService.create(userDetails.getUsername(), bookReviewCreate));
    }

    /* 독서 리뷰 삭제 */
    @DeleteMapping("/{reviewId}")
    public BaseResponse<Void> delete(
            @PathVariable long reviewId,
            @AuthenticationPrincipal UserDetails userDetails) {
        bookReviewService.delete(reviewId, userDetails.getUsername());
        return new BaseResponse<>();
    }

    /* 독서 리뷰 수정 */
    @PutMapping
    public BaseResponse<Void> update(
            @RequestBody @Valid BookReviewUpdate bookReviewUpdate,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetails userDetails) {
        bookReviewService.update(bookReviewUpdate, reviewId, userDetails.getUsername());
        return new BaseResponse<>();
    }

}
