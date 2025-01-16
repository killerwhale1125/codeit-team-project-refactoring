package com.gathering.gathering_review.domain;

import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.review.domain.StatusType;
import com.gathering.user.domain.UserDomain;

public class GatheringReviewDomain {
    private long id;
    private UserDomain user;
    private GatheringDomain gathering;
    private String content;
    private int score;
    private StatusType status;
}
