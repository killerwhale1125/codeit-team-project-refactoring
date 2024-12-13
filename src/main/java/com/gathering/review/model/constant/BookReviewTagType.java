package com.gathering.review.model.constant;

import lombok.Getter;

@Getter
public enum BookReviewTagType {

    BAD("BAD", "기대 이하였어요"),
    CS("CS", "따듯한 위로를 받았어요"),
    DP("DP", "결말이 아쉬웠어요"),
    FIND("FIND", "새로운 것을 발견했어요"),
    FUN("FUN", "정말 흥미진진했어요"),
    KL("KL", "유용한 지식을 얻었어요"),
    SAD("SAD", "눈물이 날 뻔 했어요"),
    TIME("TIME", "시간 가는 줄 몰랐어요");

    private final String type;
    private final String description;

    // Constructor
    BookReviewTagType(String type, String description) {
        this.type = type;
        this.description = description;
    }

}
