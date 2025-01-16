package com.gathering.book_review.domain;

import com.gathering.review.domain.StatusType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class BookReviewCreate {
    private long bookId;
    private String title;
    private String apprCd; // 평가
    private String tag;
    @NotEmpty
    private String content;
    private int score;
    private StatusType tmprStrgYN; // 임시저장 여부
}
