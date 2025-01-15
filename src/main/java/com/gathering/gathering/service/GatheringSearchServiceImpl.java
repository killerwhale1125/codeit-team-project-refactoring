package com.gathering.gathering.service;

import com.gathering.challenge.domain.ChallengeDomain;
import com.gathering.challenge.service.port.ChallengeRepository;
import com.gathering.challengeuser.domain.ChallengeUserDomain;
import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.controller.port.GatheringSearchService;
import com.gathering.gathering.controller.response.GatheringResponse;
import com.gathering.gathering.controller.response.GatheringSearchResponse;
import com.gathering.gathering.domain.*;
import com.gathering.gathering.service.dto.GatheringPageResponse;
import com.gathering.gathering.service.dto.GatheringSliceResponse;
import com.gathering.gathering.service.port.GatheringAsync;
import com.gathering.gathering.service.port.GatheringSearchRepository;
import com.gathering.gathering.util.GatheringDtoMapper;
import com.gathering.gathering.util.GatheringSearchActions;
import com.gathering.gatheringuser.domain.GatheringUserStatus;
import com.gathering.review.model.dto.ReviewListDto;
import com.gathering.review.service.port.ReviewRepository;
import com.gathering.user.domain.UserDomain;
import com.gathering.user.service.port.UserRepository;
import com.gathering.util.string.FullTextIndexParser;
import com.gathering.util.string.StringUtil;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gathering.common.base.response.BaseResponseStatus.INVALID_SEARCH_WORD;
import static com.gathering.common.base.response.BaseResponseStatus.UNKNOWN_SEARCH_TYPE;

@Service
@Builder
@RequiredArgsConstructor
public class GatheringSearchServiceImpl implements GatheringSearchService {

    private final GatheringSearchRepository gatheringSearchRepository;
    private final GatheringAsync gatheringAsync;
    private final GatheringSearchActions gatheringSearchActions;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ChallengeRepository challengeRepository;

    @Override
    public GatheringSearchResponse findGatheringsByFilters(GatheringSearch gatheringSearch, int page, int size, UserDetails userDetails) {
        GatheringSliceResponse gatheringSliceResponse = gatheringSearchRepository.findGatherings(gatheringSearch, page, size);

        Set<Long> wishGatheringIds
                = userDetails != null ? userRepository.findWishGatheringIdsByUserName(userDetails.getUsername()) : new HashSet<>();

        return GatheringDtoMapper.convertToGatheringSearchResponse(gatheringSliceResponse, wishGatheringIds);
    }

    @Override
    public GatheringSearchResponse findJoinableGatherings(GatheringSearch gatheringSearch, int page, int size, UserDetails userDetails) {
        GatheringSliceResponse gatheringSliceResponse = gatheringSearchRepository.findJoinableGatherings(gatheringSearch, page, size);

        Set<Long> wishGatheringIds
                = userDetails != null ? userRepository.findByUsername(userDetails.getUsername()).getWishGatheringIds() : new HashSet<>();

        return GatheringDtoMapper.convertToGatheringSearchJoinableResponse(gatheringSliceResponse, wishGatheringIds);
    }

    @Override
    public GatheringResponse getById(Long gatheringId, String userKey, UserDetails userDetails) {
        GatheringDomain gathering = gatheringSearchRepository.getByIdWithChallengeAndBook(gatheringId);

        // 조회수 비동기 처리
        gatheringAsync.incrementViewCount(gathering.getId(), userKey);

        Set<Long> wishGatheringIds
                = userDetails != null ? userRepository.findByUsername(userDetails.getUsername()).getWishGatheringIds() : new HashSet<>();

        return GatheringResponse.from(gathering, wishGatheringIds.contains(gathering.getId()));
    }

    @Override
    public GatheringSearchResponse findMyGatherings(String username, int page, int size, GatheringStatus gatheringStatus, GatheringUserStatus gatheringUserStatus) {
        GatheringPageResponse gatheringPageResponse = gatheringSearchRepository.findGatheringsForUserByUsername(username, page, size, gatheringStatus, gatheringUserStatus);

        Map<Long, Double> challengeReadingRateMap = getReadingRateMap(username, gatheringPageResponse);

        return GatheringDtoMapper.convertToMyGatheringPage(gatheringPageResponse, challengeReadingRateMap);
    }

    @Override
    public GatheringSearchResponse findMyCreated(String username, int page, int size) {
        UserDomain user = userRepository.findByUsername(username);
        GatheringPageResponse gatheringPageResponse = gatheringSearchRepository.findMyCreated(user.getUserName(), page, size);

        Map<Long, Double> challengeReadingRateMap = getReadingRateMap(username, gatheringPageResponse);

        return GatheringDtoMapper.convertToMyGatheringPage(gatheringPageResponse, challengeReadingRateMap);
    }

    @Override
    public GatheringSearchResponse findMyWishes(String username, int page, int size) {
        Set<Long> wishGatheringIds = userRepository.findWishGatheringIdsByUserName(username);

        if (wishGatheringIds.isEmpty()) {
            return GatheringSearchResponse.empty();
        }

        GatheringPageResponse gatheringPageResponse = gatheringSearchRepository.findMyWishes(wishGatheringIds, page, size);

        Map<Long, Double> challengeReadingRateMap = getReadingRateMap(username, gatheringPageResponse);

        return GatheringDtoMapper.convertToMyGatheringPage(gatheringPageResponse, challengeReadingRateMap);
    }

    @Override
    public GatheringResponse introduce(Long gatheringId) {
        GatheringDomain gathering = gatheringSearchRepository.getByIdWithChallengeAndBook(gatheringId);

        UserDomain user = userRepository.findByUsername(gathering.getOwner());

        return GatheringResponse.introduceFromEntity(gathering, user);
    }

    @Override
    public ReviewListDto review(Long gatheringId, GatheringReviewSortType sort, int page, int size) {
        return gatheringSearchRepository.getGatheringReviewList(gatheringId, sort, page, size);
    }

    /**
     * 검색 시 첫 검색은 integrated로 호출
     */
    @Override
    public GatheringSearchResponse getIntegratedResultBySearchWordAndType(String searchWord, SearchType searchType, int page, int size, String username) {
        // 2글자 미만일 경우 X
        if (!StringUtil.isValidLength(searchWord, 2)) {
            throw new BaseException(INVALID_SEARCH_WORD);
        }
        
        // Full Text 인덱스에 맞는 검색 조건으로 변환
        String formatSearchWord = FullTextIndexParser.formatForFullTextQuery(searchWord);

        /**
         * 모임 리스트 및 카운트 조회
         */
        GatheringPageResponse gatherings = findGatheringsBySearchWordAndType(formatSearchWord, searchType, page, size);

        /**
         * 리뷰 목록 및 카운트 조회
         */
        ReviewListDto reviews = reviewRepository.searchReviews(searchType, searchWord, page, size, username);

        return GatheringDtoMapper.convertToIntegratedResultPages(gatherings, reviews);
    }

    /**
     * integrated 이후 따로 검색
     */
    @Override
    public GatheringSearchResponse getGatheringsBySearchWordAndType(String searchWord, SearchType searchType, int page, int size) {
        // 2글자 미만일 경우 X
        if (!StringUtil.isValidLength(searchWord, 2)) {
            throw new BaseException(INVALID_SEARCH_WORD);
        }

        // Full Text 인덱스에 맞는 검색 조건으로 변환
        String formatSearchWord = FullTextIndexParser.formatForFullTextQuery(searchWord);
        GatheringPageResponse gatherings = findGatheringsBySearchWordAndType(formatSearchWord, searchType, page, size);

        return GatheringDtoMapper.convertToGatheringsResultPage(gatherings, gatherings.getTotalCount());
    }

    @Override
    public GatheringSearchResponse getReviewsBySearchWordAndType(String searchWord, SearchType searchType, int page, int size, String username) {
        /**
         * 리뷰 목록 및 카운트 조회
         */
        ReviewListDto reviews = reviewRepository.searchReviews(searchType, searchWord, page, size, username);

        return GatheringSearchResponse.reviewsResultPage(reviews.getBookReviews(), reviews.getTotal());
    }

    private Map<Long, Double> getReadingRateMap(String username, GatheringPageResponse gatheringPageResponse) {
        // Gathering에서 페치조인한 Challenge 정보에서 Id 리스트를 가져온다.
        List<Long> challengeIds = gatheringPageResponse.getGatherings().stream()
                .map(gathering -> gathering.getChallenge().getId())
                .collect(Collectors.toList());

        /**
         * 챌린지 ID 리스트로 ChallengeUser와 User를 한번 더 페치조인하여 조회한다.
         * 한번 더 조회하는 이유
         * - 이미 Gathering을 조회할 때 GatheringUsers 1대다 페치조인 했기 때문에 ChallengeUser는 분리시켰다.
          */
        List<ChallengeDomain> challenges = challengeRepository.getByIdsIn(challengeIds);

        /**
         * Challenge Id를 키값으로 한 Map을 생성한다.
         * Value는 해당 모임의 챌린지에 참여중인 유저의 독서 달성률
         * key - challengeId
         * value - 50.0 %
         */
        return challenges.stream()
                .collect(Collectors.toMap(
                        ChallengeDomain::getId, // Key: Challenge ID
                        challenge -> challenge.getChallengeUsers().stream()
                                .filter(challengeUser -> challengeUser.getUser().getUserName().equals(username)) // 사용자 ID 매칭
                                .findFirst() // ChallengeUser는 1명뿐이므로 첫 번째 요소를 가져옴
                                .map(ChallengeUserDomain::getReadingRate)
                                .orElse(0.0) // 매칭되지 않는 경우 null로 처리
                ));
    }

    private GatheringPageResponse findGatheringsBySearchWordAndType(String searchWord, SearchType searchType, int page, int size) {
        GatheringPageResponse gatherings = null;
        switch (searchType) {
            case TITLE -> gatherings = gatheringSearchRepository.findGatheringsBySearchWordAndTypeTitle(searchWord, page, size);
            case CONTENT -> gatherings = gatheringSearchRepository.findGatheringsBySearchWordAndTypeContent(searchWord, page, size);
            case BOOK_NAME -> gatherings = gatheringSearchRepository.findGatheringsBySearchWordAndTypeBookName(searchWord, page, size);
            default -> throw new BaseException(UNKNOWN_SEARCH_TYPE);
        };

        return gatherings;
    }

}
