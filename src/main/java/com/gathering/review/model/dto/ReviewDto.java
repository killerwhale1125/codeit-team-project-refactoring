package com.gathering.review.model.dto;

import com.gathering.review.model.entitiy.BookReview;
import com.gathering.review.model.entitiy.GatheringReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ReviewDto {

    private BookReviewDto bookReviewDto;
    private GatheringReviewDto gatheringReviewDto;

}
