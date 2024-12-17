package com.gathering.review.util;

import com.gathering.review.model.constant.ReviewSearchType;
import com.gathering.review.model.constant.StatusType;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Component;

import static com.gathering.review.model.entitiy.QBookReview.bookReview;

@Component
public class ReviewQueryBuilder {

    public BooleanBuilder buildReviewSearch(ReviewSearchType type, String param) {
        BooleanBuilder builder = new BooleanBuilder();

        if(type.equals(ReviewSearchType.BOOK)) {
            if(param != null && !param.isEmpty()) {
                builder.and(bookReview.book.title.like("%"+ param + "%"));
            }
        } else if(type.equals(ReviewSearchType.TITLE)) {
            if(param != null && !param.isEmpty()) {
                builder.and(bookReview.title.like("%"+ param + "%"));
            }
        } else {
            if(param != null && !param.isEmpty()) {
                builder.and(bookReview.content.like("%"+ param + "%"));
            }
        }

        builder.and(bookReview.status.eq(StatusType.Y));

        return builder;
    }
}
