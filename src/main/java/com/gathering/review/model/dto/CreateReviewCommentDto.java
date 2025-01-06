package com.gathering.review.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateReviewCommentDto {

    @NotNull
    private long reviewId;

    @NotEmpty
    private String content;

    private long parent;

}
