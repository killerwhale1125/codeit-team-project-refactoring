package com.gathering.gathering.service.port;

import com.gathering.gathering.domain.GatheringSearch;
import com.gathering.gathering.infrastructure.entity.Gathering;
import com.gathering.gathering.infrastructure.entity.GatheringReviewSortType;
import com.gathering.gathering.domain.GatheringStatus;
import com.gathering.gathering.infrastructure.entity.GatheringUserStatus;
import com.gathering.review.model.dto.ReviewListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;
import java.util.Set;

public interface GatheringSearchRepository {
    Slice<Gathering> findGatherings(GatheringSearch gatheringSearch, Pageable pageable);

    Page<Gathering> findGatheringsForUserByUsername(String username, Pageable pageable, GatheringStatus gatheringStatus, GatheringUserStatus gatheringUserStatus);

    Page<Gathering> findMyCreated(String username, Pageable pageable);

    Page<Gathering> findMyWishes(Set<Long> wishGatheringIds, Pageable pageable);

    ReviewListDto getGatheringReviewList(Long gatheringId, GatheringReviewSortType sort, Pageable pageable);

    Slice<Gathering> findJoinableGatherings(GatheringSearch gatheringSearch, Pageable pageable);

    Optional<Gathering> getGatheringWithChallengeAndBook(Long gatheringId);
}
