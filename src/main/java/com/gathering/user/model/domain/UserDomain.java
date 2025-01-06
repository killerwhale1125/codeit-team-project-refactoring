package com.gathering.user.model.domain;

import com.gathering.challengeuser.model.entity.ChallengeUser;
import com.gathering.gathering.model.entity.GatheringUser;
import com.gathering.review.model.entitiy.BookReview;
import com.gathering.review.model.entitiy.ReviewLikes;
import com.gathering.user.model.entitiy.User;
import com.gathering.user.model.entitiy.UserAttendance;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Builder
public class UserDomain {
    private long id;

    private String userName;

    private String password;

    private String email;

    private String profile;

    private String roles; // USER, ADMIN

    private List<UserAttendance> userAttendances;

    private List<ReviewLikes> reviewLikes;

    private List<GatheringUser> gatheringUsers = new ArrayList<>();

    private List<ChallengeUser> challengeUsers = new ArrayList<>();

    private List<BookReview> reviews = new ArrayList<>();

    private Set<Long> wishGatheringIds = new HashSet<>();

    public static UserDomain toEntity(User user) {
        return null;
    }
}
