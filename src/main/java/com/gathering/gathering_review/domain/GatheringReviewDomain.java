package com.gathering.gathering_review.domain;

import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.book_review.domain.StatusType;
import com.gathering.user.domain.UserDomain;
import com.gathering.util.date.DateHolder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.gathering.common.base.response.BaseResponseStatus.REVIEW_OWNER_MISMATCH;

@Getter
@Builder
public class GatheringReviewDomain {
    private Long id;
    private UserDomain user;
    private GatheringDomain gathering;
    private String content;
    private int score;
    private StatusType status;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public static GatheringReviewDomain create(GatheringReviewCreate gatheringReviewCreate, GatheringDomain gathering, UserDomain user) {
        return GatheringReviewDomain.builder()
                .user(user)
                .gathering(gathering)
                .content(gatheringReviewCreate.getContent())
                .score(gatheringReviewCreate.getScore())
                .status(StatusType.Y)
                .build();
    }

    public GatheringReviewDomain delete(String username) {
        validateCreatorOrThrow(username);
        this.status = StatusType.N;
        return this;
    }

    public GatheringReviewDomain update(GatheringReviewUpdate gatheringReviewUpdate, String username, DateHolder dateHolder) {
        validateCreatorOrThrow(username);
        this.content = gatheringReviewUpdate.getContent();
        this.score = gatheringReviewUpdate.getScore();
        this.modifiedTime = dateHolder.localDateTimeNow();

        return this;
    }

    private void validateCreatorOrThrow(String username) {
        if (this.user.getUserName().equals(username)) {
            throw new BaseException(REVIEW_OWNER_MISMATCH);
        }
    }

}
