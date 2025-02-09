package com.gathering.book_review.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class BookReviewUpdate {
    private long bookId;
    private String title;
    private String apprCd; // 평가
    private String tag;
    @NotEmpty
    private String content;
    private int score;
    private String tmprStrgYN; // 임시저장 여부
}
