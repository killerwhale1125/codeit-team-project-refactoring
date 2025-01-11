package com.gathering.user.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.challengeuser.model.entity.ChallengeUser;
import com.gathering.gatheringuser.model.entity.GatheringUser;
import com.gathering.review.model.entitiy.BookReview;
import com.gathering.review.model.entitiy.ReviewLikes;
import com.gathering.user.model.entitiy.UserAttendance;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDomain {
    private long id;

    private String userName;

    private String password;

    private String email;

    private String profile;

    private String roles; // USER, ADMIN

    private List<UserAttendance> userAttendances;

    private List<ReviewLikes> reviewLikes;

    private List<GatheringUser> gatheringUsers;

    private List<ChallengeUser> challengeUsers;

    private List<BookReview> reviews;

    private Set<Long> wishGatheringIds;

    public static void wishGathering(UserDomain user, Long gatheringId) {
        Set<Long> gatheringIds = user.getWishGatheringIds();
        if(gatheringIds.contains(gatheringId)) {
            gatheringIds.remove(gatheringId);
        } else {
            gatheringIds.add(gatheringId);
        }
    }
}
