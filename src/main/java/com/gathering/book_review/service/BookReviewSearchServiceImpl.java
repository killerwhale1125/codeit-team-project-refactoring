package com.gathering.book_review.service;

import com.gathering.book.controller.response.BookResponse;
import com.gathering.book.domain.BookDomain;
import com.gathering.book_review.controller.port.BookReviewSearchService;
import com.gathering.book_review.controller.response.BookReviewDetailsResponse;
import com.gathering.book_review.controller.response.BookReviewsResponse;
import com.gathering.book_review.domain.BookReviewTagType;
import com.gathering.book_review.infrastructure.BookReviewSearchRepository;
import com.gathering.gathering.domain.SearchType;
import com.gathering.gathering.service.port.GatheringSearchRepository;
import com.gathering.user.domain.UserDomain;
import com.gathering.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookReviewSearchServiceImpl implements BookReviewSearchService {

    private final BookReviewSearchRepository bookReviewSearchRepository;
    private final UserRepository userRepository;
    private final GatheringSearchRepository gatheringSearchRepository;

    @Override
    public BookReviewsResponse myReviews(String username, int page, int size) {
        UserDomain user = userRepository.findByUsername(username);
        return bookReviewSearchRepository.getMyReviews(user, page, size);
    }

    @Override
    public BookReviewsResponse bookReviews(String username) {
        return bookReviewSearchRepository.getBookReviews(username);
    }

    @Override
    public BookReviewsResponse reviewsByTag(String username, BookReviewTagType tag, int page, int size) {
        return bookReviewSearchRepository.findReviewsByTag(username, tag, page, size);
    }

    @Override
    public BookReviewDetailsResponse detail(long reviewId, String username) {
        return bookReviewSearchRepository.getDetailBy(reviewId, username);
    }

    @Override
    public BookReviewsResponse searchByWord(SearchType type, String searchWord, String username, int page, int size) {
        return bookReviewSearchRepository.searchByWord(type, searchWord, username, page, size);
    }

    @Override
    public List<BookResponse> unReviewedBooks(String username) {
        UserDomain user = userRepository.findByUsername(username);

        /* 내가 참여했던 모임 중 완료된 모임의 책 Id 정보 리스트를 먼저 가져온다. */
        List<Long> bookIds = gatheringSearchRepository.findCompletedGatheringBookIdsByUserId(user.getId());

        return bookReviewSearchRepository.getWroteReviewBooksCount(bookIds, user.getId()).stream()
                .map(BookResponse::fromEntity).collect(Collectors.toList());
    }

}
