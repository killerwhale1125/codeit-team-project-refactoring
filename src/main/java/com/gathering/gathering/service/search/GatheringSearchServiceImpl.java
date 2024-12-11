package com.gathering.gathering.service.search;

import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.dto.GatheringSearch;
import com.gathering.gathering.model.dto.GatheringSearchResponse;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringStatus;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.gathering.redis.GatheringRedisTemplate;
import com.gathering.gathering.repository.search.GatheringSearchJpaRepository;
import com.gathering.gathering.service.GatheringSearchAsync;
import com.gathering.user.model.entitiy.User;
import com.gathering.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.gathering.common.base.response.BaseResponseStatus.NON_EXISTED_GATHERING;

@Service
@RequiredArgsConstructor
public class GatheringSearchServiceImpl implements GatheringSearchService {

    private final GatheringSearchJpaRepository gatheringSearchJpaRepository;
    private final GatheringSearchAsync gatheringSearchAsync;
    private final GatheringRedisTemplate gatheringRedisTemplate;
    private final UserRepository userRepository;

    @Override
    public GatheringSearchResponse findGatherings(GatheringSearch gatheringSearch, Pageable pageable) {
        Slice<Gathering> slice = gatheringSearchJpaRepository.findGatherings(gatheringSearch, pageable);
        List<GatheringResponse> gatheringResponses = slice.getContent().stream()
                .map(GatheringResponse::fromEntity)
                .collect(Collectors.toList());
        boolean hasNext = slice.hasNext();

        return GatheringSearchResponse.fromEntity(gatheringResponses, hasNext);
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

        List<GatheringResponse> gatheringResponses = result.getContent().stream()
                .map(GatheringResponse::myGatheringFromEntity)
                .collect(Collectors.toList());

        return GatheringSearchResponse.myGatheringsFromEntity(gatheringResponses, result.getTotalElements());
    }

    @Override
    public GatheringSearchResponse findMyCreatedGatherings(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username);

        Page<Gathering> result = gatheringSearchJpaRepository.findMyCreatedGatherings(user.getId(), pageable);

        List<GatheringResponse> gatheringResponses = result.getContent().stream()
                .map(GatheringResponse::myGatheringFromEntity)
                .collect(Collectors.toList());

        return GatheringSearchResponse.myGatheringsFromEntity(gatheringResponses, result.getTotalElements());
    }

    @Override
    public GatheringSearchResponse findTop5Gatherings() {

        return null;
    }
}
