package com.gathering.gathering.util;

import com.gathering.gathering.domain.GatheringStatus;
import com.gathering.gathering.domain.GatheringWeek;
import com.gathering.gathering.domain.ReadingTimeGoal;
import com.gathering.gathering.controller.response.GatheringResultPageResponse;
import com.gathering.gathering.controller.response.GatheringSearchResponse;
import com.gathering.gathering.infrastructure.GatheringSearchJpaRepository;
import com.gathering.book_review.controller.response.BookReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GatheringSearchActions {

    private final GatheringSearchJpaRepository gatheringSearchJpaRepository;

    public List<Long> convertToGatheringIds(Page<Object[]> gatherings) {
        return Arrays.stream(gatherings.getContent().toArray(new Object[0][]))
                .map(row -> (Long) row[0])
                .collect(Collectors.toList());
    }

    public GatheringSearchResponse convertToGatheringsResultPage(Page<Object[]> gatherings, long totalElements) {
        /**
         * 검색한 모임 리스트 DTO 변환
         */
        List<GatheringResultPageResponse> gatheringResultPageResponses = gatherings.getContent().stream()
                .map(this::convertRowToGatheringResultPageResponse)
                .collect(Collectors.toList());

        return GatheringSearchResponse.gatheringsResultPage(gatheringResultPageResponses, totalElements);
    }

    private GatheringResultPageResponse convertRowToGatheringResultPageResponse(Object[] row) {
        return new GatheringResultPageResponse(
                (Long) row[0],  // GATHERING_ID
                (String) row[1], // NAME
                (Integer) row[2], // CURRENT_CAPACITY
                (Integer) row[3], // MAX_CAPACITY
                GatheringWeek.valueOf((String) row[4]).getWeek(), // GATHERING_WEEK
                GatheringStatus.valueOf((String) row[5]),
                (Date) row[6],
                ReadingTimeGoal.valueOf((String) row[7]).getMinutes(), // READING_TIME_GOAL
                (String) row[8], // IMAGE_URL
                (Long) row[9], // BOOK_ID
                (String) row[10],  // BOOK TITLE
                (String) row[11]   // BOOK IMAGE
        );
    }

    public GatheringSearchResponse convertToReviewsResultPage(Page<BookReviewDto> reviews) {
        return GatheringSearchResponse.reviewsResultPage(reviews.getContent(), reviews.getTotalElements());
    }

}
