package com.gathering.book_review_comment.controller;

import com.gathering.book_review_comment.controller.port.BookReviewCommentService;
import com.gathering.book_review_comment.controller.response.BookReviewCommentResponse;
import com.gathering.book_review_comment.domain.BookReviewCommentCreate;
import com.gathering.book_review_comment.domain.BookReviewCommentUpdate;
import com.gathering.common.base.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book/{bookId}/review-comment")
public class BookReviewCommentController {

    private final BookReviewCommentService bookReviewCommentService;

    /* 댓글 생성 */
    @PostMapping
    public BaseResponse<BookReviewCommentResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid BookReviewCommentCreate bookReviewCommentCreate) {
        return new BaseResponse<>(bookReviewCommentService.create(userDetails.getUsername(), bookReviewCommentCreate));
    }

    /* 댓글 삭제 */
    @DeleteMapping("/{commentId}")
    public BaseResponse<Void> delete(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        bookReviewCommentService.delete(commentId, userDetails.getUsername());
        return new BaseResponse<>();
    }

    /* 댓글 수정 */
    @PutMapping("/{commentId}")
    public BaseResponse<Void> update(
            @PathVariable long commentId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid BookReviewCommentUpdate bookReviewCommentUpdate) {
        bookReviewCommentService.update(commentId, userDetails.getUsername(), bookReviewCommentUpdate);
        return new BaseResponse<>();
    }
}
