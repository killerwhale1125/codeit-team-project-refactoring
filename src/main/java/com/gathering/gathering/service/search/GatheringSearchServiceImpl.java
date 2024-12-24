package com.gathering.gathering.service.search;

import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.dto.GatheringSearch;
import com.gathering.gathering.model.dto.GatheringSearchResponse;
import com.gathering.gathering.model.entity.*;
import com.gathering.gathering.repository.search.GatheringSearchJpaRepository;
import com.gathering.gathering.service.GatheringSearchAsync;
import com.gathering.gathering.util.GatheringSearchActions;
import com.gathering.review.model.dto.BookReviewDto;
import com.gathering.review.model.dto.ReviewListDto;
import com.gathering.review.repository.ReviewRepository;
import com.gathering.user.model.entitiy.User;
import com.gathering.user.repository.UserRepository;
import com.gathering.util.string.FullTextIndexParser;
import com.gathering.util.string.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.gathering.common.base.response.BaseResponseStatus.INVALID_SEARCH_WORD;
import static com.gathering.common.base.response.BaseResponseStatus.NON_EXISTED_GATHERING;

@Service
@RequiredArgsConstructor
public class GatheringSearchServiceImpl implements GatheringSearchService {

    private final GatheringSearchJpaRepository gatheringSearchJpaRepository;
    private final GatheringSearchAsync gatheringSearchAsync;
    private final GatheringSearchActions gatheringSearchActions;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public GatheringSearchResponse findGatherings(GatheringSearch gatheringSearch, Pageable pageable) {
        Slice<Gathering> slice = gatheringSearchJpaRepository.findGatherings(gatheringSearch, pageable);
        return gatheringSearchActions.convertToGatheringSearchResponse(slice);
    }

    @Override
    public GatheringResponse getById(Long gatheringId, String userKey) {
        Gathering gathering = gatheringSearchJpaRepository.getGatheringWithChallengeAndBook(gatheringId)
                .orElseThrow(() -> new BaseException(NON_EXISTED_GATHERING));

        // 조회수 비동기 처리
        gatheringSearchAsync.incrementViewCount(gathering.getId(), userKey);

        return GatheringResponse.fromEntity(gathering);
    }

    @Override
    public GatheringSearchResponse findMyGatherings(String username, Pageable pageable, GatheringStatus gatheringStatus, GatheringUserStatus gatheringUserStatus) {
        Page<Gathering> result = gatheringSearchJpaRepository.findGatheringsForUserByUsername(username, pageable, gatheringStatus, gatheringUserStatus);
        return gatheringSearchActions.convertToMyGatheringPage(result);
    }

    @Override
    public GatheringSearchResponse findMyCreated(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username);
        Page<Gathering> result = gatheringSearchJpaRepository.findMyCreated(user.getUserName(), pageable);
        return gatheringSearchActions.convertToMyGatheringPage(result);
    }

    @Override
    public GatheringSearchResponse findMyWishes(String username, Pageable pageable) {
        Set<Long> wishGatheringIds = userRepository.findWishGatheringIdsByUserName(username);

        if (wishGatheringIds.isEmpty()) {
            return GatheringSearchResponse.empty();
        }

        Page<Gathering> result = gatheringSearchJpaRepository.findMyWishes(wishGatheringIds, pageable);
        return gatheringSearchActions.convertToMyGatheringPage(result);
    }

    @Override
    public GatheringResponse introduce(Long gatheringId) {
        Gathering gathering = gatheringSearchJpaRepository.getGatheringWithChallengeAndBook(gatheringId)
                .orElseThrow(() -> new BaseException(NON_EXISTED_GATHERING));

        User user = userRepository.findByUsername(gathering.getOwner());

        return GatheringResponse.introduceFromEntity(gathering, user);
    }

    @Override
    public ReviewListDto review(Long gatheringId, GatheringReviewSortType sort, Pageable pageable) {
        return gatheringSearchJpaRepository.getGatheringReviewList(gatheringId, sort, pageable);
    }

    /**
     * 검색 시 첫 검색은 integrated로 호출
     */
    @Override
    public GatheringSearchResponse getIntegratedResultBySearchWordAndType(String searchWord, SearchType searchType, Pageable pageable, String username) {
        // 2글자 미만일 경우 X
        if (!StringUtil.isValidLength(searchWord, 2)) {
            throw new BaseException(INVALID_SEARCH_WORD);
        }
        
        // Full Text 인덱스에 맞는 검색 조건으로 변환
        String formatSearchWord = FullTextIndexParser.formatForFullTextQuery(searchWord);

        /**
         * 모임 리스트 및 카운트 조회
         */
        Page<Object[]> gatherings = gatheringSearchActions
                .findGatheringsBySearchWordAndType(formatSearchWord, searchType, pageable);

        /**
         * 리뷰 목록 및 카운트 조회
         */
        ReviewListDto tempReviews = reviewRepository.searchReviews(searchType, searchWord, pageable, username);
        Page<BookReviewDto> reviews = new PageImpl<>(tempReviews.getBookReviews(), pageable, tempReviews.getTotal());

        return gatheringSearchActions.convertToIntegratedResultPages(gatherings, reviews);
    }

    /**
     * integrated 이후 따로 검색
     */
    @Override
    public GatheringSearchResponse getGatheringsBySearchWordAndType(String searchWord, SearchType searchType, Pageable pageable) {
        // 2글자 미만일 경우 X
        if (!StringUtil.isValidLength(searchWord, 2)) {
            throw new BaseException(INVALID_SEARCH_WORD);
        }

        // Full Text 인덱스에 맞는 검색 조건으로 변환
        String formatSearchWord = FullTextIndexParser.formatForFullTextQuery(searchWord);

        Page<Object[]> gatherings = gatheringSearchActions
                .findGatheringsBySearchWordAndType(formatSearchWord, searchType, pageable);

        return gatheringSearchActions.convertToGatheringsResultPage(gatherings, gatherings.getTotalElements());
    }

    @Override
    public GatheringSearchResponse getReviewsBySearchWordAndType(String searchWord, SearchType searchType, Pageable pageable, String username) {
        /**
         * 리뷰 목록 및 카운트 조회
         */
        ReviewListDto tempReviews = reviewRepository.searchReviews(searchType, searchWord, pageable, username);
        Page<BookReviewDto> reviews = new PageImpl<>(tempReviews.getBookReviews(), pageable, tempReviews.getTotal());

        return gatheringSearchActions.convertToReviewsResultPage(reviews);
    }
}
