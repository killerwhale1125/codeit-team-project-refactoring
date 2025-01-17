package com.gathering.book_review.controller;

import com.gathering.book.controller.response.BookResponse;
import com.gathering.book_review.controller.port.BookReviewSearchService;
import com.gathering.book_review.controller.response.BookReviewDetailsResponse;
import com.gathering.book_review.controller.response.BookReviewsResponse;
import com.gathering.book_review.domain.BookReviewTagType;
import com.gathering.common.base.response.BaseResponse;
import com.gathering.gathering.domain.SearchType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book-review-search")
public class BookReviewSearchController {

    private final BookReviewSearchService bookReviewSearchService;

    /* 나의 독서 리뷰 조회 */
    @GetMapping("/my-reviews")
    public BaseResponse<BookReviewsResponse> myReviews(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam int page, @RequestParam int size) {
        return new BaseResponse<>(bookReviewSearchService.myReviews(userDetails.getUsername(), page, size));
    }

    /* 독서 리뷰 페이지 정보 조회 */
    @GetMapping
    public BaseResponse<BookReviewsResponse> bookReviews(@AuthenticationPrincipal UserDetails userDetails) {
        return new BaseResponse<>(bookReviewSearchService.bookReviews(getUsernameOrElseGet(userDetails)));
    }

    /* 리뷰 목록 조회(무한 스크롤) */
    @GetMapping("/tag")
    public BaseResponse<BookReviewsResponse> reviewsByTag(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam BookReviewTagType tag,
            int page, int size) {
        return new BaseResponse<>(bookReviewSearchService.reviewsByTag(getUsernameOrElseGet(userDetails), tag, page, size));
    }

    /* 독서 리뷰 상세 보기 */
    @GetMapping("/{bookReviewId}/detail")
    public BaseResponse<BookReviewDetailsResponse> detail(
            @PathVariable long reviewId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return new BaseResponse<>(bookReviewSearchService.detail(reviewId, getUsernameOrElseGet(userDetails)));
    }

    /* 독서 리뷰 검색 */
    @GetMapping("/search-word")
    public BaseResponse<BookReviewsResponse> searchByWord(
            @RequestParam @Valid SearchType type,
            @RequestParam String searchWord,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam int page,
            @RequestParam int size) {

        return new BaseResponse<>(bookReviewSearchService.searchByWord(type, searchWord, getUsernameOrElseGet(userDetails), page, size));
    }

    /* 종료된 모임에서 독서 리뷰를 작성하지 않은 책 정보 목록 */
    @GetMapping("/unreviewed-books")
    public BaseResponse<List<BookResponse>> unReviewedBooks(
            @AuthenticationPrincipal UserDetails userDetails) {
        return new BaseResponse<>(bookReviewSearchService.unReviewedBooks(userDetails.getUsername()));
    }

    private static String getUsernameOrElseGet(UserDetails userDetails) {
        return userDetails != null ? userDetails.getUsername() : null;
    }
}
