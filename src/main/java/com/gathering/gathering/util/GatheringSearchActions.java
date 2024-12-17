package com.gathering.gathering.util;

import com.gathering.challenge.model.entity.ReadingGoalTime;
import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.dto.GatheringResultPageResponse;
import com.gathering.gathering.model.dto.GatheringSearchResponse;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringWeek;
import com.gathering.gathering.model.entity.SearchType;
import com.gathering.gathering.repository.search.GatheringSearchJpaRepository;
import com.gathering.review.model.dto.ReviewListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.gathering.common.base.response.BaseResponseStatus.UNKNOWN_SEARCH_TYPE;

@Service
@RequiredArgsConstructor
public class GatheringSearchActions {

    private final GatheringSearchJpaRepository gatheringSearchJpaRepository;

    public GatheringSearchResponse convertToGatheringSearchResponse(Slice<Gathering> slice) {
        List<GatheringResponse> gatheringResponses = slice.getContent().stream()
                .map(GatheringResponse::fromEntity)
                .collect(Collectors.toList());

        boolean hasNext = slice.hasNext();

        return GatheringSearchResponse.fromEntity(gatheringResponses, hasNext);
    }

    public GatheringSearchResponse convertToMyGatheringPage(Page<Gathering> result) {
        List<GatheringResponse> gatheringResponses = result.getContent().stream()
                .map(GatheringResponse::myGatheringFromEntity)
                .collect(Collectors.toList());

        return GatheringSearchResponse.fromEntity(gatheringResponses, result.getTotalElements());
    }

    public GatheringSearchResponse convertToResultPages(Page<Object[]> gatherings, Page<ReviewListDto> reviews) {
        /**
         * 검색한 모임 리스트 DTO 변환
         */
        List<GatheringResultPageResponse> gatheringResultPageResponses = Arrays.stream(gatherings.getContent().toArray(new Object[0][]))
                .map(row -> new GatheringResultPageResponse(
                        (Long) row[0],  // GATHERING_ID
                        (String) row[1], // NAME
                        (Integer) row[2], // CURRENT_CAPACITY
                        (Integer) row[3], // MAX_CAPACITY
                        GatheringWeek.valueOf((String) row[4]), // GATHERING_WEEK
                        ReadingGoalTime.valueOf((String) row[5]), // READING_TIME_GOAL
                        (String) row[6], // IMAGE_URL
                        (Long) row[7], // BOOK_ID
                        (String) row[8],  // BOOK TITLE
                        (String) row[9]  // BOOK IMAGE
                ))
                .collect(Collectors.toList());

        /**
         * TODO - 리뷰 리스트 정보 커스텀 필요
         */

        return GatheringSearchResponse.resultPages(gatheringResultPageResponses,
                gatherings.getTotalElements(),
                reviews.getContent(),
                reviews.getTotalElements());
    }

    public Page<Object[]> findGatheringsBySearchWordAndType(String searchWord, SearchType searchType, Pageable pageable) {
        Page<Object[]> resultList = null;
        switch (searchType) {
            case TITLE -> resultList = gatheringSearchJpaRepository.findGatheringsBySearchWordAndTypeTitle(searchWord, pageable);
            case CONTENT -> resultList = gatheringSearchJpaRepository.findGatheringsBySearchWordAndTypeContent(searchWord, pageable);
            case BOOK_NAME -> resultList = gatheringSearchJpaRepository.findGatheringsBySearchWordAndTypeBookName(searchWord, pageable);
            default -> throw new BaseException(UNKNOWN_SEARCH_TYPE);
        };

        return resultList;
    }

    public List<Long> convertToGatheringIds(Page<Object[]> gatherings) {
        return Arrays.stream(gatherings.getContent().toArray(new Object[0][]))
                .map(row -> (Long) row[0])
                .collect(Collectors.toList());
    }
}
