package com.gathering.gathering.service.search;

import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.dto.GatheringResultPage;
import com.gathering.gathering.model.dto.GatheringSearch;
import com.gathering.gathering.model.dto.GatheringSearchResponse;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringReviewSortType;
import com.gathering.gathering.model.entity.GatheringStatus;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.gathering.repository.search.GatheringSearchJpaRepository;
import com.gathering.gathering.service.GatheringSearchAsync;
import com.gathering.gathering.util.GatheringSearchActions;
import com.gathering.review.model.dto.ReviewListDto;
import com.gathering.user.model.entitiy.User;
import com.gathering.user.repository.UserRepository;
import com.gathering.util.string.FullTextIndexParser;
import com.gathering.util.string.StringUtil;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        return GatheringResponse.introduceFromEntity(gathering);
    }

    @Override
    public ReviewListDto review(Long gatheringId, GatheringReviewSortType sort, Pageable pageable) {
        return gatheringSearchJpaRepository.getGatheringReviewList(gatheringId, sort, pageable);
    }

    @Override
    public GatheringSearchResponse searchByTitle(String searchWord, Pageable pageable) {
        if (!StringUtil.isValidLength(searchWord, 3)) {
            throw new BaseException(INVALID_SEARCH_WORD);
        }
        Page<Tuple> tuples
                = gatheringSearchJpaRepository.findGatheringsBySearchWord(FullTextIndexParser.formatForFullTextQuery(searchWord), pageable);
        Page<GatheringResultPage> result = gatheringSearchActions.convertToResultPage(tuples, pageable);
        return GatheringSearchResponse.resultPages(result.getContent(), result.getTotalElements());
    }
}
