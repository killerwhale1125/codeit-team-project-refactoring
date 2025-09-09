package com.gathering.gathering.service.port;

import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.gathering.domain.GatheringSearch;
import com.gathering.gathering.domain.GatheringStatus;
import com.gathering.gathering.service.dto.GatheringPageResponse;
import com.gathering.gathering.service.dto.GatheringSliceResponse;
import com.gathering.gathering_user.domain.GatheringUserStatus;

import java.util.List;
import java.util.Set;

public interface GatheringSearchRepository {
    GatheringSliceResponse findGatherings(GatheringSearch gatheringSearch, int page, int size);

    GatheringPageResponse findGatheringsForUserByUsername(String username, int page, int size, GatheringStatus gatheringStatus, GatheringUserStatus gatheringUserStatus);

    GatheringPageResponse findMyCreated(String username, int page, int size);

    GatheringPageResponse findMyWishes(Set<Long> wishGatheringIds, int page, int size);

    GatheringDomain getByIdWithChallengeAndBook(Long gatheringId);

    GatheringPageResponse findGatheringsBySearchWordAndTypeTitle(String searchWord, int page, int size);

    GatheringPageResponse findGatheringsBySearchWordAndTypeContent(String searchWord, int page, int size);

    GatheringPageResponse findGatheringsBySearchWordAndTypeBookName(String searchWord, int page, int size);

    List<Long> findCompletedGatheringBookIdsByUserId(Long userId);
}
