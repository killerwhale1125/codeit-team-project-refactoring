package com.gathering.mock.fake.repository;

import com.gathering.gathering.model.dto.GatheringSearch;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringReviewSortType;
import com.gathering.gathering.model.entity.GatheringStatus;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.gathering.repository.search.GatheringSearchRepository;
import com.gathering.review.model.dto.ReviewListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;
import java.util.Set;

public class FakeGatheringSearchRepository implements GatheringSearchRepository {
    @Override
    public Slice<Gathering> findGatherings(GatheringSearch gatheringSearch, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Gathering> findGatheringsForUserByUsername(String username, Pageable pageable, GatheringStatus gatheringStatus, GatheringUserStatus gatheringUserStatus) {
        return null;
    }

    @Override
    public Page<Gathering> findMyCreated(String username, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Gathering> findMyWishes(Set<Long> wishGatheringIds, Pageable pageable) {
        return null;
    }

    @Override
    public ReviewListDto getGatheringReviewList(Long gatheringId, GatheringReviewSortType sort, Pageable pageable) {
        return null;
    }

    @Override
    public Slice<Gathering> findJoinableGatherings(GatheringSearch gatheringSearch, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Gathering> getGatheringWithChallengeAndBook(Long gatheringId) {
        return Optional.empty();
    }
}
