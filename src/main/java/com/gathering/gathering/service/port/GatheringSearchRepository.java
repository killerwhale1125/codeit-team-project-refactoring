package com.gathering.gathering.service.port;

import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.gathering.domain.GatheringReviewSortType;
import com.gathering.gathering.domain.GatheringSearch;
import com.gathering.gathering.domain.GatheringStatus;
import com.gathering.gathering.service.dto.GatheringPageResponse;
import com.gathering.gathering.service.dto.GatheringSliceResponse;
import com.gathering.gatheringuser.domain.GatheringUserStatus;
import com.gathering.review.model.dto.ReviewListDto;

import java.util.Set;

public interface GatheringSearchRepository {
    GatheringSliceResponse findGatherings(GatheringSearch gatheringSearch, int page, int size);

    GatheringPageResponse findGatheringsForUserByUsername(String username, int page, int size, GatheringStatus gatheringStatus, GatheringUserStatus gatheringUserStatus);

    GatheringPageResponse findMyCreated(String username, int page, int size);

    GatheringPageResponse findMyWishes(Set<Long> wishGatheringIds, int page, int size);

    ReviewListDto getGatheringReviewList(Long gatheringId, GatheringReviewSortType sort, int page, int size);

    GatheringSliceResponse findJoinableGatherings(GatheringSearch gatheringSearch, int page, int size);

    GatheringDomain getByIdWithChallengeAndBook(Long gatheringId);

    GatheringPageResponse findGatheringsBySearchWordAndTypeTitle(String searchWord, int page, int size);

    GatheringPageResponse findGatheringsBySearchWordAndTypeContent(String searchWord, int page, int size);

    GatheringPageResponse findGatheringsBySearchWordAndTypeBookName(String searchWord, int page, int size);
}
